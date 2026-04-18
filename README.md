# java-upgrade-demo

Minimal sample project used to demo the Devin **multi-agent fan-out** pattern on a
framework upgrade from **Java 11 + Spring Boot 2.7** to **Java 17 + Spring Boot 3.x**.

The `main` branch intentionally contains the *"old"* version with just enough
classic upgrade pain points for a short, live demo:

- `pom.xml` with `<java.version>11</java.version>` and Spring Boot `2.7.x`
- `javax.servlet` import in a controller
- Spring Security configured with the (now-removed) `WebSecurityConfigurerAdapter`
- A legacy test written with JUnit 4 + `SpringRunner`
- A GitHub Actions workflow pinned to `actions/setup-java@v2` and Java `11`

The upgrade is performed on a dedicated branch and opened as a PR so every diff
is visible in one place. The slim baseline keeps a live demo run under ~5 minutes.

## Build

```bash
./mvnw clean verify
```

## Run

```bash
./mvnw spring-boot:run
# GET http://localhost:8080/api/greet           (anonymous)
# GET http://localhost:8080/some-other-path     (basic auth: user / password)
```
