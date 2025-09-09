# 07 HTTP - Webclient Module (Kotlin)

[← Back to \[07 HTTP\]'s README](../README.md)

## Implementation Details

- WebClient Configuration
  - Default configuration settings [`DefaultConfiguration`](./src/main/kotlin/com/fresult/client/configs/DefaultConfiguration.kt)
  - Authenticated configuration with basic auth filter [`AuthenticatedConfiguration`](./src/main/kotlin/com/fresult/client/configs/AuthenticatedConfiguration.kt)
  - Client properties configuration [`ClientProperties`](./src/main/kotlin/com/fresult/client/ClientProperties.kt)

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:07-http:webclient:clean\
            :kotlin:07-http:webclient:build
```

### Running Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:07-http:webclient:bootRun
```

[← Back to \[07 HTTP\]'s README](../README.md)
