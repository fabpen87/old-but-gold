# Upgrade Guide — Java 11 → 17 · Spring Boot 2.7 → 3

This guide is the **canonical before/after reference** for the `main` → `upgrade/java-17-spring-boot-3` migration. Every section shows the exact diff you should apply. Line numbers refer to the **baseline** files on `main`.

Related pages:
- **Architecture Diagrams** — high-level view of what the code does (unchanged by the upgrade).
- **Troubleshooting & Common Errors** — what to do when a diff in this guide produces a compile/runtime error.
- **Post-Upgrade Verification Checklist** — what "green" looks like after applying all diffs.

---

## 1. `pom.xml` — parent, Java version, Lombok bump

Bump the Spring Boot parent, `<java.version>`, `<maven.compiler.{source,target}>`, and Lombok. MapStruct 1.5.3 is already on a Jakarta-compatible version — no change needed.

```diff
 <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
-    <version>2.7.18</version>
+    <version>3.2.5</version>
     <relativePath/>
 </parent>
 ...
 <properties>
-    <java.version>11</java.version>
-    <maven.compiler.source>11</maven.compiler.source>
-    <maven.compiler.target>11</maven.compiler.target>
+    <java.version>17</java.version>
+    <maven.compiler.source>17</maven.compiler.source>
+    <maven.compiler.target>17</maven.compiler.target>
     <org.mapstruct.version>1.5.3.Final</org.mapstruct.version>
-    <lombok.version>1.18.22</lombok.version>
+    <lombok.version>1.18.30</lombok.version>
 </properties>
```

Reference: `pom.xml` lines 8-27 on the baseline.

Drop `junit-vintage-engine` once every JUnit 4 test is migrated (see section 5):

```diff
-<dependency>
-    <groupId>org.junit.vintage</groupId>
-    <artifactId>junit-vintage-engine</artifactId>
-    <scope>test</scope>
-</dependency>
```

Reference: `pom.xml` lines 74-78.

---

## 2. `SecurityConfig.java` — WebSecurityConfigurerAdapter → SecurityFilterChain bean

The adapter class is removed in Spring Security 6. Declare a `SecurityFilterChain` bean and switch to the lambda DSL; rename `antMatchers` → `requestMatchers`.

### Before (baseline, `src/main/java/com/example/demo/config/SecurityConfig.java` lines 1-22)

```java
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/greet").permitAll()
                .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
}
```

### After

```java
package com.example.demo.config;

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

Changes:
- `extends WebSecurityConfigurerAdapter` removed.
- `configure(HttpSecurity)` replaced by a `@Bean SecurityFilterChain filterChain(...)` that returns `http.build()`.
- `authorizeRequests()` → `authorizeHttpRequests(...)`.
- `antMatchers` → `requestMatchers`.
- Chained `.and()` calls replaced with lambda DSL (`csrf(csrf -> csrf.disable())`, `httpBasic(Customizer.withDefaults())`).

---

## 3. `User.java` — `javax.persistence` & `javax.validation` → `jakarta.*`

### Before (baseline, `src/main/java/com/example/demo/domain/User.java` lines 1-18)

```java
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Objects;
```

### After

```java
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;
```

The annotations (`@Entity`, `@Id`, `@GeneratedValue(...)`, `@Email`, `@NotBlank`, `@Size(...)`) do not change — only the imported package.

---

## 4. `UserDto.java` & `UserController.java` — `javax.validation.*` → `jakarta.validation.*`

### `UserDto.java` (baseline lines 8-10)

```diff
-import javax.validation.constraints.Email;
-import javax.validation.constraints.NotBlank;
-import javax.validation.constraints.Size;
+import jakarta.validation.constraints.Email;
+import jakarta.validation.constraints.NotBlank;
+import jakarta.validation.constraints.Size;
```

### `UserController.java` (baseline line 16)

```diff
-import javax.validation.Valid;
+import jakarta.validation.Valid;
```

---

## 5. `GreetingController.java` — `javax.servlet` → `jakarta.servlet`

Baseline line 6:

```diff
-import javax.servlet.http.HttpServletRequest;
+import jakarta.servlet.http.HttpServletRequest;
```

`HttpServletRequest.getRequestURL()` behaves identically — only the package name changes.

---

## 6. Tests — JUnit 4 → JUnit 5

### Before (`src/test/java/com/example/demo/legacy/LegacyJunit4Test.java` lines 1-23)

```java
package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LegacyJunit4Test {

    @Autowired
    private GreetingController greetingController;

    @Test
    public void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
```

### After

```java
package com.example.demo.legacy;

import com.example.demo.web.GreetingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LegacyJunit4Test {

    @Autowired
    private GreetingController greetingController;

    @Test
    void greetingControllerIsLoaded() {
        assertNotNull(greetingController);
    }
}
```

Changes:
- `@RunWith(SpringRunner.class)` dropped — JUnit 5 picks up `@SpringBootTest` via the `SpringExtension` automatically.
- `org.junit.Test` / `org.junit.Assert.assertNotNull` → `org.junit.jupiter.api.Test` / `org.junit.jupiter.api.Assertions.assertNotNull`.
- Class and method downgraded from `public` to package-private (JUnit 5 does not require `public`).

`DemoApplicationTests` and `UserControllerTest` are already on JUnit 5 (`src/test/java/com/example/demo/DemoApplicationTests.java` lines 3-4, `src/test/java/com/example/demo/web/UserControllerTest.java` line 4) — no change needed.

After the migration, remove `junit-vintage-engine` from `pom.xml` (see section 1).

---

## 7. `.github/workflows/ci.yml` — Java 11 → 17

### Before (`.github/workflows/ci.yml` lines 10-16)

```yaml
      - uses: actions/checkout@v3

      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          distribution: adopt
          java-version: '11'
```

### After

```yaml
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven
```

Changes:
- `actions/checkout@v3` → `@v4` (latest, Node 20 runtime).
- `actions/setup-java@v2` → `@v4`. `@v2` is deprecated and its Maven cache adapter is broken, which is why the baseline omits `cache:`.
- `distribution: adopt` → `distribution: temurin` (Adoptium rebrand).
- `java-version: '11'` → `'17'`.
- Re-add `cache: maven` — `setup-java@v4` ships a working cache adapter, which dramatically speeds up CI.

---

## 8. Putting it all together

Run locally on the branch that contains all the diffs above:

```bash
./mvnw clean verify
```

Expected output (the important lines):

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Then walk through the **Post-Upgrade Verification Checklist** to make sure nothing was missed.

---

## Sources

- `pom.xml` lines 8-27, 74-78
- `src/main/java/com/example/demo/config/SecurityConfig.java` lines 1-22
- `src/main/java/com/example/demo/domain/User.java` lines 1-18
- `src/main/java/com/example/demo/dto/UserDto.java` lines 8-10
- `src/main/java/com/example/demo/web/UserController.java` line 16
- `src/main/java/com/example/demo/web/GreetingController.java` line 6
- `src/test/java/com/example/demo/legacy/LegacyJunit4Test.java` lines 1-23
- `.github/workflows/ci.yml` lines 10-16
