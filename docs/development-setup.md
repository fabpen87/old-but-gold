# Development Environment Setup

How to build, run, and develop `java-upgrade-demo` on a laptop. The baseline targets **Java 11**; the upgrade PR bumps the target to Java 17.

---

## Prerequisites

| Tool | Version | Why |
| :--- | :--- | :--- |
| JDK | **11** (Temurin / AdoptOpenJDK recommended) | The `pom.xml` pins `<java.version>11</java.version>` on lines 22-24. Any newer JDK *will* build (Java 11 bytecode is forward-compatible) but the build targets 11-class files. |
| Maven | **not required** | The repo ships the Maven Wrapper (`./mvnw`, `./mvnw.cmd`). Let the wrapper download the matching Maven version into `~/.m2/wrapper`. |
| Git | any modern version | Standard. |
| curl or HTTPie | optional | For exercising the REST endpoints. |

JDK 17 is required only if you are working on the upgrade branch; the baseline builds fine on 11 or 17.

### Installing JDK 11 (Temurin)

| OS | Command |
| :--- | :--- |
| macOS | `brew install --cask temurin@11` |
| Ubuntu / Debian | `sudo apt-get install -y temurin-11-jdk` (after adding the Adoptium apt repo) or `sudo apt-get install -y openjdk-11-jdk` |
| Windows | Download the MSI installer from [https://adoptium.net](https://adoptium.net) |

Verify:

```bash
java -version
# openjdk version "11.0.xx" ...
```

---

## Clone and build

```bash
git clone https://github.com/fabpen87/old-but-gold.git
cd old-but-gold
./mvnw clean verify
```

`clean verify` runs the full lifecycle: compile, annotation processing (Lombok → MapStruct — see the **Annotation Processing Deep Dive**), test execution (JUnit 5 + JUnit Vintage for the legacy JUnit 4 test), and packaging. A green `BUILD SUCCESS` is the smoke test.

The wrapper downloads Maven on the first run, then caches it in `~/.m2/wrapper`.

---

## Run the application

```bash
./mvnw spring-boot:run
```

The app binds to `http://localhost:8080` (`application.yml` line 15). Spring Security prints the auto-configured HTTP Basic password to the console only when `spring.security.user.password` is **not** set — in this project it *is* set (`application.yml` line 13, `password`), so nothing is printed.

Smoke-test the two endpoints:

```bash
# public
curl http://localhost:8080/api/greet
# -> Hello from http://localhost:8080/api/greet

# secured
curl -u user:password http://localhost:8080/api/users
# -> []
```

See the **API Reference** page for the full endpoint list and response formats.

---

## IntelliJ IDEA

1. **Import** the project: `File → Open → select the cloned repo's `pom.xml` → Open as Project`. IDEA detects the Maven wrapper and synchronises automatically.
2. **Enable annotation processing** (required for Lombok and MapStruct):
   `Settings / Preferences → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing`.
3. **Install the Lombok plugin:** `Settings → Plugins → Marketplace → Lombok`. Restart IDEA. The plugin makes IDEA aware of the generated getters/setters/builders without needing `delombok`.
4. **Project SDK:** `File → Project Structure → Project → SDK: Temurin 11` (or `Temurin 17` on the upgrade branch).
5. **Target bytecode:** `Project Structure → Project → Language level: 11 (or 17)`.
6. **Run configurations:** IDEA automatically creates a `DemoApplication` run configuration from `src/main/java/com/example/demo/DemoApplication.java` lines 6-11.

MapStruct-specific tip: IDEA regenerates implementations on every incremental compile; if you ever see `No implementation was created for UserMapper` in IDEA (but not on `./mvnw`), toggle `Build → Rebuild Project`.

---

## VS Code

Recommended extensions (install from the Extensions panel):

| Extension | Purpose |
| :--- | :--- |
| **Extension Pack for Java** (Microsoft) | Language server, debugger, test runner, Maven integration. |
| **Lombok Annotations Support for VS Code** | Teaches the Java language server about the generated accessors. Prevents false-positive "cannot resolve symbol" errors. |
| **Spring Boot Extension Pack** | Boot dashboard, live application info, starters wizard. |

After installation, open the cloned repo. VS Code picks up the Maven wrapper automatically. If the Java language server errors out with "Annotation processing not supported", open the Command Palette → `Java: Clean Java Language Server Workspace`.

---

## Useful Maven targets

```bash
./mvnw clean compile                    # just compile (no tests)
./mvnw clean test                       # run tests
./mvnw clean verify                     # full build (default)
./mvnw spring-boot:run                  # run the app
./mvnw dependency:tree                  # show resolved dependency graph
./mvnw dependency:tree -Dincludes=jakarta  # only jakarta branches (useful during the upgrade)
./mvnw -DskipTests package              # build the fat jar without running tests
```

The resulting jar is `target/java-upgrade-demo-0.0.1-SNAPSHOT.jar` and can be executed with `java -jar target/java-upgrade-demo-0.0.1-SNAPSHOT.jar`.

---

## Verifying your setup works

After `./mvnw clean verify` exits `BUILD SUCCESS`, you should see:

1. `target/java-upgrade-demo-0.0.1-SNAPSHOT.jar` — the Spring Boot fat jar.
2. `target/generated-sources/annotations/com/example/demo/mapper/UserMapperImpl.java` — the MapStruct-generated class (evidence that annotation processing ran successfully).
3. Surefire test reports at `target/surefire-reports/*.txt` with at least:
   - `com.example.demo.DemoApplicationTests` (JUnit 5, context loads)
   - `com.example.demo.legacy.LegacyJunit4Test` (JUnit 4 via Vintage)
   - `com.example.demo.web.UserControllerTest` (JUnit 5, MockMvc)

Any missing piece is covered by **Troubleshooting & Common Errors**.

---

## Sources

- `README.md` lines 23-32
- `pom.xml` lines 22-27 (Java version, MapStruct / Lombok versions)
- `pom.xml` lines 95-114 (annotation-processor configuration)
- `src/main/resources/application.yml` lines 1-15
- `src/main/java/com/example/demo/DemoApplication.java` lines 1-12
- `mvnw`, `mvnw.cmd` (Maven wrapper scripts)
