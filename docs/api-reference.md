# API Reference

Full REST API documentation for `java-upgrade-demo` on the `main` branch (Java 11 + Spring Boot 2.7.18). The API exposes two controllers: a public greeting endpoint and a secured user-management CRUD endpoint.

All endpoints are served by embedded Tomcat on port `8080` (configured at `src/main/resources/application.yml` line 15). Authenticated endpoints use **HTTP Basic** authentication with the credentials from `application.yml` lines 11-13 (defaults: `user` / `password`).

---

## Overview table

| Method | Path | Auth | Response type | Source |
| :--- | :--- | :--- | :--- | :--- |
| `GET`  | `/api/greet`       | none          | `text/plain`       | `GreetingController.java` lines 11-14 |
| `GET`  | `/api/users`       | HTTP Basic    | `application/json` | `UserController.java` lines 28-31 |
| `GET`  | `/api/users/{id}`  | HTTP Basic    | `application/json` | `UserController.java` lines 33-39 |
| `POST` | `/api/users`       | HTTP Basic    | `application/json` | `UserController.java` lines 41-46 |

Authorization rules are declared in `SecurityConfig.java` lines 14-20: `/api/greet` is `permitAll()`, every other route requires authentication, and CSRF is disabled globally so non-browser clients do not have to negotiate a token.

---

## `GET /api/greet`

Public diagnostic endpoint that echoes the full request URL back to the caller. Useful as a smoke test after a deploy.

- **Auth:** none (`SecurityConfig.java` line 17, `antMatchers("/api/greet").permitAll()`).
- **Response:** `200 OK`, `Content-Type: text/plain;charset=UTF-8`, body `Hello from <request URL>`.
- **Source:** `src/main/java/com/example/demo/web/GreetingController.java` lines 11-14.

### Example

```bash
curl -i http://localhost:8080/api/greet
```

```
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8

Hello from http://localhost:8080/api/greet
```

### Notes

- The body is built from `HttpServletRequest.getRequestURL()`, so reverse-proxy headers that rewrite scheme/host (`X-Forwarded-*`) are not honoured unless the proxy terminates TLS and Spring Boot is configured with `server.forward-headers-strategy: framework`.
- The import is `javax.servlet.http.HttpServletRequest` on the Java 11 baseline (`GreetingController.java` line 6). On Spring Boot 3 this becomes `jakarta.servlet.http.HttpServletRequest`.

### Sources

- `src/main/java/com/example/demo/web/GreetingController.java` lines 1-15
- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 14-21

---

## `GET /api/users`

List every `User` in the database as an array of `UserDto`.

- **Auth:** HTTP Basic. Without credentials the Spring Security filter chain returns `401 Unauthorized` with a `WWW-Authenticate: Basic realm="Realm"` challenge.
- **Response:** `200 OK`, `Content-Type: application/json`, body is a JSON array of `UserDto`.
- **Source:** `src/main/java/com/example/demo/web/UserController.java` lines 28-31.

### Example — authenticated

```bash
curl -i -u user:password http://localhost:8080/api/users
```

```
HTTP/1.1 200
Content-Type: application/json

[
  {"id":1,"name":"Ada","email":"ada@example.com"},
  {"id":2,"name":"Alan","email":"alan@example.com"}
]
```

### Example — unauthenticated

```bash
curl -i http://localhost:8080/api/users
```

```
HTTP/1.1 401
WWW-Authenticate: Basic realm="Realm"
```

### Notes

- The controller delegates to `repository.findAll()` and runs the result list through `UserMapper.toDtoList(...)` (`UserMapper.java` line 18) so clients never see the JPA entity directly.
- The response preserves insertion order because `JpaRepository.findAll()` does not apply an explicit `ORDER BY`; rely on `GET /api/users/{id}` if you need deterministic access.

### Sources

- `src/main/java/com/example/demo/web/UserController.java` lines 20-31
- `src/main/java/com/example/demo/mapper/UserMapper.java` lines 10-19
- `src/main/java/com/example/demo/domain/UserRepository.java` lines 3-6

---

## `GET /api/users/{id}`

Fetch a single `User` by its primary key.

- **Auth:** HTTP Basic (same rules as `GET /api/users`).
- **Path variable:** `id` — `Long`, the database primary key.
- **Responses:**
  - `200 OK` with a `UserDto` JSON body when the user exists.
  - `404 Not Found` with an empty body when no user matches the id.
- **Source:** `src/main/java/com/example/demo/web/UserController.java` lines 33-39.

### Example — found

```bash
curl -i -u user:password http://localhost:8080/api/users/1
```

```
HTTP/1.1 200
Content-Type: application/json

{"id":1,"name":"Ada","email":"ada@example.com"}
```

### Example — not found

```bash
curl -i -u user:password http://localhost:8080/api/users/999
```

```
HTTP/1.1 404
```

### Implementation detail

The controller uses `Optional` chaining to map the repository result into either a `200` or a `404`:

```java
return repository.findById(id)
        .map(mapper::toDto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
```

This is the idiomatic way to avoid `if (opt.isPresent())` and still produce the correct HTTP status.

### Sources

- `src/main/java/com/example/demo/web/UserController.java` lines 33-39
- `src/main/java/com/example/demo/mapper/UserMapper.java` lines 13-14
- `src/main/java/com/example/demo/domain/UserRepository.java` lines 3-6

---

## `POST /api/users`

Create a new user.

- **Auth:** HTTP Basic.
- **Request body:** `UserDto` JSON. Validation is enforced by `@Valid` (`UserController.java` line 42) using the Bean Validation constraints declared on `UserDto.java` lines 20-26:
  - `name` — `@NotBlank`, `@Size(min = 2, max = 80)`.
  - `email` — `@NotBlank`, `@Email`.
  - `id` is ignored on creation (MapStruct maps it to `null` because of `@Mapping(target = "id", ignore = true)` on `UserMapper.java` line 15).
- **Responses:**
  - `201 Created` with `Location: /api/users/{id}` and a `UserDto` body that includes the assigned `id`.
  - `400 Bad Request` when the body fails validation.
- **Source:** `src/main/java/com/example/demo/web/UserController.java` lines 41-46.

### Example — happy path

```bash
curl -i -u user:password \
     -H "Content-Type: application/json" \
     -d '{"name":"Ada","email":"ada@example.com"}' \
     http://localhost:8080/api/users
```

```
HTTP/1.1 201
Location: /api/users/1
Content-Type: application/json

{"id":1,"name":"Ada","email":"ada@example.com"}
```

### Example — invalid payload

```bash
curl -i -u user:password \
     -H "Content-Type: application/json" \
     -d '{"name":"","email":"not-an-email"}' \
     http://localhost:8080/api/users
```

```
HTTP/1.1 400
Content-Type: application/json

{"timestamp":"...","status":400,"error":"Bad Request","path":"/api/users"}
```

### Notes

- The `@PostMapping` relies on CSRF being disabled in `SecurityConfig.java` line 15. With CSRF enabled you would need to send a token on every write request.
- Validation responses are the Spring default because the project does not register a `@ControllerAdvice` — see **Known Limitations & Technical Debt**.
- The validation constraint imports are under `javax.validation.constraints.*` on the baseline (`UserDto.java` lines 8-10). On Spring Boot 3 they become `jakarta.validation.constraints.*`.

### Sources

- `src/main/java/com/example/demo/web/UserController.java` lines 41-46
- `src/main/java/com/example/demo/dto/UserDto.java` lines 8-26
- `src/main/java/com/example/demo/mapper/UserMapper.java` lines 15-16
- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 14-21
