# Configuration Reference

Every property that ships in the baseline configuration file `src/main/resources/application.yml`. The file is 15 lines long and is loaded automatically by Spring Boot from the classpath at startup.

---

## Full file

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1
    username: sa
    password: ""
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  security:
    user:
      name: user
      password: password
server:
  port: 8080
```

Sources: `src/main/resources/application.yml` lines 1-15.

---

## Property-by-property reference

| Property | Value | Line | Purpose |
| :--- | :--- | :--- | :--- |
| `spring.datasource.url` | `jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1` | 3 | JDBC URL for the H2 database. `mem:demo` makes it an in-memory database named `demo`. `DB_CLOSE_DELAY=-1` keeps the database alive for the entire JVM lifetime so the data does not disappear when the last connection is closed. |
| `spring.datasource.username` | `sa` | 4 | H2 default admin user. |
| `spring.datasource.password` | `""` (empty) | 5 | H2 default password. Empty on purpose because H2 is in-memory and never exposed. |
| `spring.jpa.hibernate.ddl-auto` | `update` | 8 | Hibernate schema management strategy. `update` compares the entity model with the current schema on startup and applies the diff. See the alternatives table below. |
| `spring.jpa.show-sql` | `false` | 9 | When `true`, every SQL statement Hibernate executes is logged to stdout. Useful for debugging, noisy in production. |
| `spring.security.user.name` | `user` | 12 | Default HTTP Basic username consumed by Spring Boot's auto-configured `InMemoryUserDetailsManager`. |
| `spring.security.user.password` | `password` | 13 | Default HTTP Basic password. Plaintext on purpose for this demo — see **Known Limitations & Technical Debt**. |
| `server.port` | `8080` | 15 | Port embedded Tomcat binds to. |

---

## `spring.jpa.hibernate.ddl-auto` — when to pick which value

| Value | Behaviour | Good for |
| :--- | :--- | :--- |
| `none` | Hibernate never touches the schema. | Production systems where schema changes ship via Flyway / Liquibase. |
| `validate` | Hibernate verifies the schema matches the entity model and fails to start if it drifts. | Production systems that want an extra safety net. |
| `update` *(current)* | Hibernate creates missing tables/columns and additively updates the schema. Does not drop columns. | Demos, local development. |
| `create` | Drop and recreate the schema on every startup. | Volatile integration tests. |
| `create-drop` | `create` on startup + drop on shutdown. | Spring Boot tests (default for in-memory DBs). |

For any real service, move to Flyway or Liquibase and set `ddl-auto: none` or `validate`. The `update` setting is convenient here because the demo is designed to be thrown away.

---

## Security credentials

The HTTP Basic credentials live in `application.yml` lines 11-13. When Spring Security sees `spring.security.user.name` / `spring.security.user.password` set, it builds an `InMemoryUserDetailsManager` with one user and the `USER` role, and that user authenticates every `anyRequest().authenticated()` route declared in `SecurityConfig.java` line 18.

The password is stored in plain text; see the **Known Limitations & Technical Debt** page for the recommended production replacement (BCrypt-hashed users via `UserDetailsService`, or an external IdP).

---

## Overrides and profiles

The project does **not** ship additional profile-specific files (no `application-dev.yml`, no `application-prod.yml`). Spring Boot still honours the usual override precedence:

1. Command-line arguments: `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090`.
2. Environment variables: `SERVER_PORT=9090 ./mvnw spring-boot:run`.
3. External config files: `./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.config.import=optional:file:./local.yml`.
4. The packaged `application.yml` (the values documented here).

Anyone introducing a production profile should also review **Known Limitations & Technical Debt** — the plaintext credentials and disabled CSRF are acceptable only for the in-memory demo profile.

---

## H2 console

The baseline deliberately does **not** enable the H2 web console. To enable it locally, add these two properties (do not commit them):

```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2
```

…and either authenticate through HTTP Basic or add `.requestMatchers("/h2/**").permitAll()` to the Security configuration. See the **Database Schema & Data Model** page for a full walk-through.

### Sources

- `src/main/resources/application.yml` lines 1-15
- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 14-21
- `pom.xml` lines 47-50 (H2 runtime dependency)
- `pom.xml` lines 42-45 (`spring-boot-starter-security` which enables the HTTP Basic default)
