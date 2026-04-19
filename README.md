# java-upgrade-demo

Sample project used to demonstrate an **upgrade from Java 11 + Spring Boot 2.7** to
**Java 17 + Spring Boot 3.x**.

The `main` branch intentionally contains the *"old"* version with the classic pain
points you hit during an upgrade:

- `pom.xml` with `<java.version>11</java.version>` and Spring Boot `2.7.x`
- `javax.persistence`, `javax.validation`, `javax.servlet` imports
- An old Lombok version (`1.18.22`) that fails on JDK 17 without a bump
- MapStruct 1.5.3 (fine) but coupled with the `javax` annotations
- A legacy test written with JUnit 4 + `SpringRunner`
- Spring Security configured with the (now-deprecated) `WebSecurityConfigurerAdapter`
- A GitHub Actions workflow pinned to `actions/setup-java@v2` and Java `11`

The upgrade is performed in a dedicated branch and opened as a PR so you can see
every diff in one place.

## Build

```bash
./mvnw clean verify
```

## Run

```bash
./mvnw spring-boot:run
# GET http://localhost:8080/api/greet
# GET http://localhost:8080/api/users   (basic auth: user / password printed in logs)
```

## Documentation

Detailed reference documentation lives under [`docs/`](./docs) and feeds the generated wiki:

- [Architecture Diagrams](docs/architecture.md) — request flow, package structure, compile-time code generation
- [API Reference](docs/api-reference.md) — every REST endpoint with curl examples
- [Configuration Reference](docs/configuration-reference.md) — every property in `application.yml`
- [Database Schema & Data Model](docs/database-schema.md) — `users` table DDL, id strategy, H2 console
- [Development Environment Setup](docs/development-setup.md) — JDK, IDE, Maven wrapper, smoke tests
- [Upgrade Guide — Java 11 → 17, Spring Boot 2.7 → 3](docs/upgrade-guide.md) — exhaustive before/after diffs
- [Post-Upgrade Verification Checklist](docs/post-upgrade-checklist.md) — operational sign-off checklist
- [Troubleshooting & Common Errors](docs/troubleshooting.md) — real error messages and fixes
- [Known Limitations & Technical Debt](docs/known-limitations.md) — what is deliberately skipped
- [Annotation Processing Deep Dive](docs/annotation-processing.md) — Lombok + MapStruct ordering
