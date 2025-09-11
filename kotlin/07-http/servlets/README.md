# 07 HTTP - Servlets Module (Kotlin)

[← Back to \[07 HTTP\]'s README](../README.md)

## Implementation Details

- Tomcat Integration
  - Demo Spring WebFlux application running on Tomcat instead of default Reactor Netty [`build.gradle.kts#L10`](./build.gradle.kts)
  - Main application setup [`TomcatWebfluxApplication`](./src/main/kotlin/com/fresult/servlets/TomcatWebfluxApplication.kt)
- Endpoint Implementations
  - Annotation-based controller approach [`GreetingsController`](./src/main/kotlin/com/fresult/servlets/GreetingsController.kt)
  - Functional router configuration with co-routines [`GreetingsRouteConfiguration`](./src/main/kotlin/com/fresult/servlets/GreetingsRouteConfiguration.kt)

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:07-http:servlets:clean\
              :kotlin:07-http:servlets:build
```

### Running Application

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:07-http:servlets:bootRun
```

[← Back to \[07 HTTP\]'s README](../README.md)
