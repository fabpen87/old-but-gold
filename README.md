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
