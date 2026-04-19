# Annotation Processing Deep Dive

This page explains how Lombok and MapStruct cooperate during compilation, why the declared order matters, and what changes when the versions of either processor move forward.

Pair this page with the **Architecture Diagrams** (section 3, "Compile-Time Code Generation") for the visual summary.

---

## The two processors involved

| Processor | Version (baseline) | Role | Coordinate |
| :--- | :--- | :--- | :--- |
| Lombok | `1.18.22` | Generates `get*`/`set*`/`toString`/`builder`/constructors on types annotated with `@Getter`, `@Setter`, `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@ToString`. | `org.projectlombok:lombok:${lombok.version}` |
| MapStruct | `1.5.3.Final` | Generates a `UserMapperImpl` implementation for the `UserMapper` interface by reading the public accessors of `User` and `UserDto`. | `org.mapstruct:mapstruct-processor:${org.mapstruct.version}` |

Both versions are declared as `<properties>` in `pom.xml` lines 25-26.

---

## Why the order matters

MapStruct's code generator walks the **public API** of the source and target types to figure out how to copy data. It calls `User.getName()`, `User.getEmail()`, `User.getId()`, and so on — **methods that do not exist in the hand-written `.java` source**. Those accessors are generated at compile time by Lombok from `@Getter`, `@Setter`, `@Data` on `User.java` lines 23-28 and `UserDto.java` lines 12-15.

Annotation processing is not atomic: the compiler invokes each processor in turn during the same compilation round. If MapStruct runs **before** Lombok, it inspects the (still accessor-less) `User` class and fails with:

```
[ERROR] UserMapper.java:[11,17] No implementation was created for UserMapper due to having
a problem in the erroneous element com.example.demo.domain.User. Hint: this often means that
some other annotation processor failed.
```

The `maven-compiler-plugin` runs processors in the order they are declared in `<annotationProcessorPaths>`. Declaring Lombok **first** guarantees accessors exist by the time MapStruct runs.

### The critical snippet (`pom.xml` lines 101-112)

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

Swap those two `<path>` entries and the build fails on every incremental compile.

---

## What happens if the order is reversed

The compilation sequence, step by step:

1. Round 1: `javac` asks MapStruct to process `@Mapper` types. MapStruct calls `User.getName()` via reflection on the model — **the method does not yet exist** because Lombok has not run.
2. MapStruct emits the `No implementation was created for UserMapper` error and adds `UserMapperImpl` to the **excluded** set.
3. Round 2: `javac` asks Lombok to process `@Getter`/`@Setter`. Lombok writes the accessors, but MapStruct has already bailed.
4. The build ends with a missing `UserMapperImpl`, which causes **`NoSuchBeanDefinitionException: UserMapper`** at application startup — because Spring cannot autowire a non-existent bean into `UserController` (`UserController.java` line 26).

This failure mode is especially confusing because the error message points at `User.java`, not at the processor ordering. If you see it, re-open `pom.xml` and verify the `<path>` order.

---

## `lombok-mapstruct-binding` — when it is needed

Newer Lombok versions (≥ 1.18.20) emit accessors that MapStruct 1.4 and earlier cannot recognize — MapStruct looks for JavaBean-style method names and older Lombok occasionally uses different internal naming. The solution is a bridge annotation processor published by the Lombok project:

```xml
<path>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok-mapstruct-binding</artifactId>
    <version>0.2.0</version>
</path>
```

Matrix:

| Lombok version | MapStruct version | `lombok-mapstruct-binding` needed? |
| :--- | :--- | :--- |
| ≥ 1.18.20 | ≥ 1.5.0 | No — MapStruct 1.5.x detects Lombok natively. |
| ≥ 1.18.20 | 1.4.x and older | Yes. |
| < 1.18.20 | any | No. |

The baseline uses Lombok `1.18.22` + MapStruct `1.5.3.Final`, so `lombok-mapstruct-binding` is **not** required. When you bump Lombok to `1.18.30` during the upgrade, the matrix is still "no bridge needed" because MapStruct is already on 1.5+.

If you ever downgrade MapStruct and upgrade Lombok, add the binding **after** Lombok and **before** MapStruct in the `<annotationProcessorPaths>`:

```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </path>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok-mapstruct-binding</artifactId>
        <version>0.2.0</version>
    </path>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </path>
</annotationProcessorPaths>
```

---

## `<dependency>` vs `<annotationProcessorPaths>`

The project declares Lombok and MapStruct in two different places:

1. `<dependencies>` (`pom.xml` lines 52-62) — classes from these jars need to be on the **runtime** classpath, or more precisely:
   - **Lombok** is marked `<scope>provided</scope>`: only the compiler needs it. Lombok does not leave any runtime bytecode in the produced classes (all the `@Getter`-style annotations are erased).
   - **MapStruct** has the default `compile` scope: the generated `UserMapperImpl` calls `org.mapstruct.factory.Mappers` helpers at runtime, so the jar must be on the runtime classpath.
2. `<annotationProcessorPaths>` (`pom.xml` lines 101-112) — classes from these jars are put on the **annotation-processor classpath**, which is isolated from the main compile classpath. This is the correct way to declare processors since `maven-compiler-plugin` 3.5.x; declaring them only in `<dependencies>` would rely on deprecated behaviour and would not give you explicit ordering control.

In short: `<dependency>` handles **runtime availability**; `<annotationProcessorPaths>` handles **compile-time execution order**. Both lists are required.

---

## IDE gotchas

- **IntelliJ IDEA** needs `Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing` turned on, and the Lombok plugin installed from the Marketplace. Without the plugin, the editor underlines every `user.getName()` in red even though `./mvnw compile` succeeds.
- **VS Code (redhat.java)** picks up `<annotationProcessorPaths>` from `pom.xml` automatically starting from the 2023 release line. If accessors are flagged as missing, run `Java: Clean Java Language Server Workspace` from the Command Palette.
- **Eclipse** requires the `m2e-apt` connector (`Preferences → Maven → Annotation Processing → Automatically configure JDT APT`).

---

## Sources

- `pom.xml` lines 25-26 (version properties)
- `pom.xml` lines 52-57 (Lombok dependency, `<scope>provided</scope>`)
- `pom.xml` lines 58-62 (MapStruct dependency)
- `pom.xml` lines 95-114 (maven-compiler-plugin configuration)
- `pom.xml` lines 101-112 (annotationProcessorPaths — Lombok before MapStruct)
- `src/main/java/com/example/demo/domain/User.java` lines 3-8, 23-28 (Lombok annotations)
- `src/main/java/com/example/demo/dto/UserDto.java` lines 3-6, 12-15 (Lombok annotations)
- `src/main/java/com/example/demo/mapper/UserMapper.java` lines 1-19 (MapStruct interface)
- `src/main/java/com/example/demo/web/UserController.java` lines 22-26 (consumer that needs the generated bean)
