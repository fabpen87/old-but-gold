# Post-Upgrade Verification Checklist

Operational checklist to confirm the Java 11 + Spring Boot 2.7 → Java 17 + Spring Boot 3 upgrade is complete. Run through every item; each one corresponds to a concrete section of the **Upgrade Guide** or the **Troubleshooting & Common Errors** page.

Work top to bottom — the list is ordered so that later items assume earlier ones pass.

---

## Build & compile

- [ ] `./mvnw clean verify` exits `BUILD SUCCESS` with zero test failures.
- [ ] The Maven output shows **JDK 17** in the banner:
  `[INFO] Building java-upgrade-demo 0.0.1-SNAPSHOT` immediately followed by `maven-compiler-plugin:...:compile` resolving to `javac 17.x`.
- [ ] No deprecation warnings for `WebSecurityConfigurerAdapter`, `authorizeRequests`, or `antMatchers`.
- [ ] `target/generated-sources/annotations/com/example/demo/mapper/UserMapperImpl.java` is present and references `jakarta.*` types where applicable (via the generated getters/setters).

## Source imports

- [ ] Zero residual imports of `javax.persistence.*`:
  ```bash
  grep -rn "import javax\.persistence" src/main src/test
  # -> no matches
  ```
- [ ] Zero residual imports of `javax.validation.*`:
  ```bash
  grep -rn "import javax\.validation" src/main src/test
  # -> no matches
  ```
- [ ] Zero residual imports of `javax.servlet.*`:
  ```bash
  grep -rn "import javax\.servlet" src/main src/test
  # -> no matches
  ```
- [ ] Zero residual imports of `javax.annotation.*` (if you add any, watch for `@PostConstruct` / `@PreDestroy`):
  ```bash
  grep -rn "import javax\.annotation" src/main src/test
  # -> no matches
  ```

## Security configuration

- [ ] `SecurityConfig.java` no longer references `WebSecurityConfigurerAdapter`:
  ```bash
  grep -rn "WebSecurityConfigurerAdapter" src/main
  # -> no matches
  ```
- [ ] `SecurityConfig.java` declares a `@Bean SecurityFilterChain filterChain(HttpSecurity http)` and returns `http.build();`.
- [ ] Every authorization rule uses `requestMatchers(...)` (not `antMatchers(...)`).
- [ ] `csrf(csrf -> csrf.disable())` and `httpBasic(Customizer.withDefaults())` are wired through the lambda DSL.

## Tests

- [ ] `junit-vintage-engine` is removed from `pom.xml`:
  ```bash
  grep -n "junit-vintage-engine" pom.xml
  # -> no matches
  ```
- [ ] No test class uses JUnit 4 artifacts:
  ```bash
  grep -rn "org\.junit\.Test\|org\.junit\.runner\|SpringRunner\|org\.junit\.Assert" src/test
  # -> no matches
  ```
- [ ] All test classes import `org.junit.jupiter.api.Test`.
- [ ] Spring Security test is still on the classpath (`pom.xml` `spring-security-test`, test scope) — `UserControllerTest.listEmpty` and `createAndGet` continue to pass.
- [ ] Surefire reports at `target/surefire-reports/` list at least 4 tests run (contextLoads, greetingControllerIsLoaded, listEmpty, createAndGet).

## `pom.xml`

- [ ] `<parent>` is on Spring Boot `3.x` (verified via `pom.xml`). `3.2.5` or later is the recommended minimum.
- [ ] `<java.version>` is `17`.
- [ ] `<maven.compiler.source>` and `<maven.compiler.target>` are both `17`.
- [ ] `<lombok.version>` is `1.18.26` or newer. `1.18.30+` preferred for JDK 17 users.
- [ ] `<org.mapstruct.version>` is `1.5.3.Final` or newer.
- [ ] `<annotationProcessorPaths>` still lists Lombok **before** MapStruct (`pom.xml` lines 101-112 on the baseline).

## CI workflow (`.github/workflows/ci.yml`)

- [ ] `actions/checkout` pinned to `@v4` (or newer).
- [ ] `actions/setup-java` pinned to `@v4`.
- [ ] `distribution` is `temurin`.
- [ ] `java-version` is `'17'`.
- [ ] `cache: maven` is set on the setup-java step.
- [ ] The `Set up JDK` step name matches the version (`Set up JDK 17`).
- [ ] The build command is unchanged: `mvn -B -ntp clean verify`.

## Endpoint smoke tests

Run the app (`./mvnw spring-boot:run`) and exercise each route. See the **API Reference** for request/response details.

- [ ] `GET /api/greet` returns `200 OK` with body `Hello from http://localhost:8080/api/greet`:
  ```bash
  curl -i http://localhost:8080/api/greet
  ```
- [ ] `GET /api/users` without credentials returns `401 Unauthorized`:
  ```bash
  curl -i http://localhost:8080/api/users
  ```
- [ ] `GET /api/users` with credentials returns `200 OK` and a JSON array:
  ```bash
  curl -i -u user:password http://localhost:8080/api/users
  ```
- [ ] `POST /api/users` with a valid body returns `201 Created` and a `Location` header:
  ```bash
  curl -i -u user:password -H "Content-Type: application/json" \
       -d '{"name":"Ada","email":"ada@example.com"}' \
       http://localhost:8080/api/users
  ```
- [ ] `POST /api/users` with an invalid body returns `400 Bad Request`:
  ```bash
  curl -i -u user:password -H "Content-Type: application/json" \
       -d '{"name":"","email":"not-an-email"}' \
       http://localhost:8080/api/users
  ```
- [ ] `GET /api/users/999` (unknown id) returns `404 Not Found`.

## Sanity greps

Quick sanity greps that combine several checks above:

```bash
grep -rn "javax\." src/main src/test | grep -v "javax\.tools\|javax\.xml\|javax\.crypto" | head
# -> no matches (we ignore javax.tools/xml/crypto which are still valid JDK packages)

grep -rn "WebSecurityConfigurerAdapter\|antMatchers\b\|authorizeRequests\b" src/main
# -> no matches

grep -rn "SpringRunner\|org\.junit\.runner\|@RunWith" src/test
# -> no matches
```

---

## Sign-off

If every checkbox is ticked and the three grep sanity commands return no matches, the upgrade is complete and ready to merge. If any item fails, jump to the matching section of **Troubleshooting & Common Errors**.
