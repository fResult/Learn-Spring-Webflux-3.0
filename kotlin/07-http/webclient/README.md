# 07 HTTP - Webclient Module (Kotlin)

[← Back to \[07 HTTP\]'s README](../README.md)

## Implementation Details

- WebClient Configuration
  - Default configuration settings [`DefaultConfiguration`](./src/main/kotlin/com/fresult/client/configs/DefaultConfiguration.kt)
  - Authenticated configuration with basic auth filter [`AuthenticatedConfiguration`](./src/main/kotlin/com/fresult/client/configs/AuthenticatedConfiguration.kt)
  - Client properties configuration [`ClientProperties`](./src/main/kotlin/com/fresult/client/ClientProperties.kt)
- Client Implementation
  - Default HTTP client for unauthenticated requests [`DefaultClient`](./src/main/kotlin/com/fresult/client/DefaultClient.kt)
  - Authenticated client with basic auth [`AuthenticatedClient`](./src/main/kotlin/com/fresult/client/AuthenticatedClient.kt)
  - Client runner for executing requests on application startup [`ClientRunner`](./src/main/kotlin/com/fresult/client/ClientRunner.kt)
  - Data model for responses [`Greeting`](./src/main/kotlin/com/fresult/client/Greeting.kt)
- Request/Response Timing
  - Timing exchange filter function for request/response metrics [`TimingExchangeFilterFunction`](./src/main/kotlin/com/fresult/client/timer/TimingExchangeFilterFunction.kt)
  - Response wrapper for timing information [`TimingClientResponseWrapper`](./src/main/kotlin/com/fresult/client/timer/TimingClientResponseWrapper.kt)
  - WebClient customizer with timing capabilities [`TimingWebClientCustomizer`](./src/main/kotlin/com/fresult/client/timer/TimingWebClientCustomizer.kt)
- Server Implementation
  - HTTP endpoints providing greeting services [`HttpController`](./src/main/kotlin/com/fresult/service/HttpController.kt)
  - Security configuration with basic authentication [`HttpSecurityConfiguration`](./src/main/kotlin/com/fresult/service/HttpSecurityConfiguration.kt)
  - Main application class for server side [`HttpServiceApplication`](./src/main/kotlin/com/fresult/service/HttpServiceApplication.kt)
  - Main application class for client side [`HttpClientApplication`](./src/main/kotlin/com/fresult/client/HttpClientApplication.kt)

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
