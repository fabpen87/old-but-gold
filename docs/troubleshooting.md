# Troubleshooting & Common Errors

Real error messages you are likely to hit while upgrading `java-upgrade-demo` from Java 11 + Spring Boot 2.7 to Java 17 + Spring Boot 3, plus the fixes. Each section ends with a "Sources" list pointing at the code that produces the error on `main`.

---

## 1. `NoClassDefFoundError: javax/persistence/Entity`

### What you see

```
Caused by: java.lang.NoClassDefFoundError: javax/persistence/Entity
    at java.base/java.lang.ClassLoader.defineClass1(Native Method)
    at org.springframework.data.repository.core.support.AbstractRepositoryMetadata.<clinit>(AbstractRepositoryMetadata.java)
```

### Why it happens

Spring Boot 3 dropped the `javax.*` Jakarta EE namespace in favour of `jakarta.*`. `javax.persistence.Entity` is no longer on the classpath; the JPA starter now ships `jakarta.persistence-api`.

Any class that still imports `javax.persistence.*` — for example the baseline `User.java` lines 10-14 — fails to load at runtime.

### Fix

Rewrite every `javax.persistence.*`, `javax.validation.*`, `javax.servlet.*`, and `javax.annotation.*` import to the `jakarta.*` equivalent. The mapping is 1:1; only the package prefix changes.

Concrete files in this project:

| File | Imports to rewrite |
| :--- | :--- |
| `src/main/java/com/example/demo/domain/User.java` | lines 10-17 (`javax.persistence.*`, `javax.validation.constraints.*`) |
| `src/main/java/com/example/demo/dto/UserDto.java` | lines 8-10 (`javax.validation.constraints.*`) |
| `src/main/java/com/example/demo/web/UserController.java` | line 16 (`javax.validation.Valid`) |
| `src/main/java/com/example/demo/web/GreetingController.java` | line 6 (`javax.servlet.http.HttpServletRequest`) |

`find` + `sed` one-liner to preview:

```bash
grep -rn "import javax\." src/main
```

### Sources

- `src/main/java/com/example/demo/domain/User.java` lines 10-17
- `src/main/java/com/example/demo/dto/UserDto.java` lines 8-10
- `src/main/java/com/example/demo/web/UserController.java` line 16
- `src/main/java/com/example/demo/web/GreetingController.java` line 6

---

## 2. `IllegalAccessError: lombok.javac.apt.LombokProcessor`

### What you see

```
java.lang.IllegalAccessError: class lombok.javac.apt.LombokProcessor
    (in unnamed module @0x...) cannot access class com.sun.tools.javac.processing.JavacProcessingEnvironment
    (in module jdk.compiler) because module jdk.compiler does not export
    com.sun.tools.javac.processing to unnamed module
```

### Why it happens

The baseline pins `lombok.version` to `1.18.22` (`pom.xml` line 26). That version predates the JDK 16+ module system restrictions and cannot access `com.sun.tools.javac.*` internals that it needs for annotation processing.

### Fix

Bump Lombok to **1.18.26 or newer**. `1.18.30` or later is recommended:

```diff
-    <lombok.version>1.18.22</lombok.version>
+    <lombok.version>1.18.30</lombok.version>
```

Modern Lombok versions declare the required `--add-opens` on `jdk.compiler/com.sun.tools.javac.*` automatically in their manifest, so no extra JVM flag is needed for Maven.

### Sources

- `pom.xml` line 26 (`lombok.version`)
- `pom.xml` lines 52-57 (Lombok runtime dependency)
- `pom.xml` lines 101-106 (Lombok inside `<annotationProcessorPaths>`)

---

## 3. `WebSecurityConfigurerAdapter` deprecated / removed

### What you see

Compile time (Spring Security 5.7+ on the baseline):

```
warning: [deprecation] WebSecurityConfigurerAdapter in org.springframework.security.config.annotation.web.configuration has been deprecated
```

Compile time (Spring Security 6.0 after the upgrade):

```
error: cannot find symbol
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
                                        ^
  symbol: class WebSecurityConfigurerAdapter
```

### Why it happens

Spring Security 6 removed `WebSecurityConfigurerAdapter` in favour of declaring a `SecurityFilterChain` bean. The baseline `SecurityConfig.java` lines 6-21 still extends the adapter.

### Fix

Rewrite `SecurityConfig` as a `@Configuration` that declares a `@Bean SecurityFilterChain`:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/greet").permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

Everything else (CSRF disabled, `/api/greet` open, HTTP Basic) stays the same.

### Sources

- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 6-21

---

## 4. `antMatchers()` not found

### What you see

```
error: cannot find symbol
    .antMatchers("/api/greet").permitAll()
     ^
  symbol:   method antMatchers(String)
```

### Why it happens

Spring Security 6 renamed `antMatchers(...)` to `requestMatchers(...)` and removed `authorizeRequests()` in favour of `authorizeHttpRequests(...)`. The baseline `SecurityConfig.java` line 17 still uses `antMatchers`.

### Fix

Rename the call and switch to the lambda DSL. The rewritten snippet in the previous section already does this. Inline diff:

```diff
-http
-    .csrf().disable()
-    .authorizeRequests()
-        .antMatchers("/api/greet").permitAll()
-        .anyRequest().authenticated()
-    .and()
-    .httpBasic();
+http
+    .csrf(csrf -> csrf.disable())
+    .authorizeHttpRequests(auth -> auth
+        .requestMatchers("/api/greet").permitAll()
+        .anyRequest().authenticated())
+    .httpBasic(Customizer.withDefaults());
```

### Sources

- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 14-20

---

## 5. `No implementation was created for UserMapper`

### What you see

```
[ERROR] UserMapper.java:[11,17] No implementation was created for UserMapper due to having
a problem in the erroneous element com.example.demo.domain.User. Hint: this often means that
some other annotation processor failed.
```

### Why it happens

MapStruct needs the Lombok-generated getters/setters on `User` and `UserDto` to exist **before** it runs. If the processor order in `pom.xml` is wrong (MapStruct declared before Lombok), MapStruct sees classes without accessors and aborts.

### Fix

Ensure Lombok is declared **first** inside `<annotationProcessorPaths>` (`pom.xml` lines 101-112):

```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </path>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </path>
</annotationProcessorPaths>
```

On Lombok ≥ 1.18.20 combined with MapStruct < 1.5.x, also add `lombok-mapstruct-binding` as an extra path. MapStruct 1.5.3 — which this project uses — no longer needs that binding. Details on the **Annotation Processing Deep Dive** page.

### Sources

- `pom.xml` lines 101-112 (processor order)
- `pom.xml` line 25 (`org.mapstruct.version`)
- `pom.xml` line 26 (`lombok.version`)

---

## 6. `InvalidTestClassError` from JUnit 4

### What you see

On Spring Boot 3 with `spring-boot-starter-test`:

```
org.junit.runners.model.InvalidTestClassError: Invalid test class 'com.example.demo.legacy.LegacyJunit4Test':
  1. The class has no public constructor. Looking for a public constructor with no parameters.
```

Or, when `junit-vintage-engine` is left out entirely:

```
WARNING: No tests found with test runner 'JUnit 4'
```

### Why it happens

Spring Boot 3's `spring-boot-starter-test` ships only JUnit Jupiter (JUnit 5) by default. `spring-boot-starter-parent:2.7.x` used to include `junit-vintage-engine` transitively; on 3.x you must add it yourself if you keep any JUnit 4 tests.

The cleaner long-term fix is to migrate JUnit 4 tests to JUnit 5.

### Fix — option A (migrate the test)

```java
// before (LegacyJunit4Test.java lines 1-23)
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LegacyJunit4Test {
    @Autowired private GreetingController greetingController;
    @Test public void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
```

```java
// after
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LegacyJunit4Test {
    @Autowired private GreetingController greetingController;
    @Test void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
```

Also remove `junit-vintage-engine` from `pom.xml` lines 74-78 once no JUnit 4 tests remain.

### Fix — option B (keep JUnit 4, add the vintage engine)

Leave the test on JUnit 4 but explicitly depend on `org.junit.vintage:junit-vintage-engine` with test scope (which the baseline already does on `pom.xml` lines 74-78). This is fine for Spring Boot 2.7; on Spring Boot 3 you should migrate.

### Sources

- `src/test/java/com/example/demo/legacy/LegacyJunit4Test.java` lines 1-23
- `pom.xml` lines 74-78 (`junit-vintage-engine`)

---

## 7. Bonus: CI workflow failing on `setup-java@v2`

### What you see

```
Run actions/setup-java@v2
Error: The action 'actions/setup-java@v2' is deprecated. Please use @v4.
```

### Fix

Bump `actions/setup-java` to `@v4`, upgrade `java-version` to `17`, and re-add `cache: maven`:

```yaml
- name: Set up JDK 17
  uses: actions/setup-java@v4
  with:
    distribution: temurin
    java-version: '17'
    cache: maven
```

### Sources

- `.github/workflows/ci.yml` lines 12-16

---

## Still stuck?

Run the **Post-Upgrade Verification Checklist** end to end. If one of the items fails, walk through the corresponding section of this page.
