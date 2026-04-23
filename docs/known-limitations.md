# Known Limitations & Technical Debt

This page catalogues deliberate simplifications in the `main` branch baseline. They are kept intentionally so the demo stays small and the upgrade story stays focused, but none of them is production-grade. Each entry includes the exact source location and what you should do in a real codebase.

---

## 1. CSRF protection is disabled globally

### Where
`src/main/java/com/example/demo/config/SecurityConfig.java` line 15: `.csrf().disable()`.

### What it means
Every state-changing request (e.g. `POST /api/users`) accepts cross-site form posts without a CSRF token. For a browser-driven app this is exploitable.

### Why it is like that
The demo is exercised exclusively via `curl` and the JUnit `MockMvc`-based tests (`UserControllerTest.createAndGet` adds `csrf()` manually on line 40). Disabling the filter keeps the `curl` examples terse.

### What to do in production
- If the API is stateless and consumed by non-browser clients (mobile, server-to-server), keep CSRF disabled but enforce a **bearer-token auth** (JWT, OAuth 2.0 Resource Server) instead of HTTP Basic.
- If the API is consumed by a browser, enable CSRF with `.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))` or use the default `HttpSessionCsrfTokenRepository` and have the frontend attach the token header.

---

## 2. Hardcoded plaintext credentials

### Where
`src/main/resources/application.yml` lines 12-13:

```yaml
spring:
  security:
    user:
      name: user
      password: password
```

### What it means
The entire "user database" is two strings committed to git. Anyone reading the repo can authenticate.

### What to do in production
- Replace with a real `UserDetailsService` backed by a database, an IdP, or a secrets manager.
- At minimum, read the value from `SPRING_SECURITY_USER_PASSWORD` via the Spring `Environment` and inject it at runtime. Never commit credentials to VCS.
- When passwords live in a database, store them BCrypt-hashed (`PasswordEncoder encoder = new BCryptPasswordEncoder();`).

---

## 3. No Spring Profiles

### Where
`src/main/resources/application.yml` is the only configuration file. There is no `application-dev.yml`, `application-prod.yml`, or `@Profile`-conditional bean in the codebase.

### What it means
Any change you make to the configuration applies to every environment. You cannot have different credentials for local vs. staging vs. production without editing the file.

### What to do in production
- Split `application.yml` into `application-dev.yml`, `application-prod.yml`, and an empty shared `application.yml`.
- Select the profile at startup: `SPRING_PROFILES_ACTIVE=prod java -jar ...` or `-Dspring.profiles.active=prod`.
- Use `@Profile("dev")` on dev-only beans (e.g. the `H2ConsoleConfig`) so they never activate in production.

---

## 4. `equals` / `hashCode` based on a nullable primary key

### Where
`src/main/java/com/example/demo/domain/User.java` lines 43-54.

### What it means
Before the entity is persisted, `id` is `null`. Once it is persisted, `id` is assigned. If a `User` is added to a `HashSet` before `save(...)` and then saved, its hash code changes and it becomes unretrievable. See the **Database Schema & Data Model** page for the long version.

### What to do in production
Pick one of:
- **Business-key-based** `equals`/`hashCode` on an immutable unique field (`email` here) — stable across persistence transitions.
- **UUID primary keys** assigned at construction time (`UUID.randomUUID()` or a `UUIDGenerator` with `@GeneratedValue`). `equals`/`hashCode` on the UUID is safe before and after persistence.
- **No `equals`/`hashCode` override at all** — rely on reference equality and never put JPA entities into hash-based collections (the safest default for DDD aggregates).

---

## 5. No `@ControllerAdvice` for centralized error handling

### Where
None of the controllers (`GreetingController`, `UserController`) or any `@ControllerAdvice` class translates exceptions into a structured error payload.

### What it means
- Validation failures return Spring Boot's default JSON error body with `timestamp`, `status`, `error`, `path` — usable but noisy and without application-specific error codes.
- Unexpected exceptions surface as `500 Internal Server Error` with a generic body; stack traces appear only in the logs.
- There is no consistent shape for clients to parse — every error is schema-free.

### What to do in production
Add a `@RestControllerAdvice` that handles at least `MethodArgumentNotValidException`, `ConstraintViolationException`, `DataIntegrityViolationException`, and a catch-all `Exception`, returning an RFC 7807 `application/problem+json` body.

---

## 6. No structured logging

### Where
The project uses Spring Boot's default logback configuration with pattern layout on stdout. There is no `logback-spring.xml`, no JSON encoder, and no MDC population.

### What it means
- Log aggregation tools (Loki, Elasticsearch, Cloud Logging) have to parse free-form text lines.
- Every request arrives without a trace id — correlating a stack trace to an upstream request is by-eye only.
- There is no request/response logging filter.

### What to do in production
- Add `logstash-logback-encoder` and a `logback-spring.xml` with a JSON encoder. Keep a plaintext profile for local dev.
- Introduce Spring Boot's auto-configured MDC population (`spring.mvc.log-request-details=true`, plus a filter that puts a request id into MDC).
- Consider Micrometer Tracing + OpenTelemetry to propagate a `traceId` across service calls.

---

## 7. `GreetingController` has no HTTP test

### Where
`src/test/java/com/example/demo/legacy/LegacyJunit4Test.java` lines 19-22 only asserts that the bean is wired (`assertNotNull(greetingController)`). There is no `MockMvc` test exercising `GET /api/greet`.

### What it means
If the request mapping, response body, or the (trivial) HTTP handling regressed silently, no test would catch it.

### What to do in production
Add a test symmetrical to `UserControllerTest`:

```java
@SpringBootTest
@AutoConfigureMockMvc
class GreetingControllerTest {

    @Autowired MockMvc mockMvc;

    @Test
    void greetReturns200AndEchoesUrl() throws Exception {
        mockMvc.perform(get("/api/greet"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("/api/greet")));
    }
}
```

---

## Out of scope — explicitly not addressed by this demo

The following concerns are **deliberately not covered** because they are orthogonal to the Java 11 → 17 upgrade story:

- Connection pooling tuning (HikariCP defaults are used).
- Caching (`@Cacheable`, Redis, Caffeine).
- Rate limiting, request throttling, backpressure.
- Observability: Actuator endpoints are **not** exposed.
- OpenAPI / Swagger UI documentation.
- Multi-tenancy, auditing (`@CreatedDate`, `@LastModifiedDate`).
- Containerization (`Dockerfile`, Buildpacks, Helm chart).

All of these are reasonable follow-ups once the upgrade is in place.

---

## Sources

- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 14-21
- `src/main/java/com/example/demo/domain/User.java` lines 43-54
- `src/main/resources/application.yml` lines 11-13
- `src/test/java/com/example/demo/legacy/LegacyJunit4Test.java` lines 1-23
- `src/test/java/com/example/demo/web/UserControllerTest.java` lines 1-47
