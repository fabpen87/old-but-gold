# java-upgrade-demo

Sample project demonstrating an upgrade from Java 11 + Spring Boot 2.7 to
**Java 17 + Spring Boot 3.x**.

Current stack:

- Java 17 (`<java.version>17</java.version>`, `maven-compiler` source/target `17`)
- Spring Boot `3.2.5` (parent pom)
- `jakarta.persistence`, `jakarta.validation`, `jakarta.servlet` imports
- Lombok `1.18.32` with `lombok-mapstruct-binding` annotation processor
- MapStruct `1.5.5.Final`
- Spring Security 6 configured via a `SecurityFilterChain` bean (no more `WebSecurityConfigurerAdapter`)
- JUnit 5 only (the legacy JUnit 4 test and dependency have been removed)
- GitHub Actions workflow on `actions/checkout@v4`, `actions/setup-java@v4`, Java `17`

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
