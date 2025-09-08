# Webflux Module

## Implementation Details

- HTTP Controllers
    - Using `@RestController` and `@RequestMapping` with [`CustomerRestController`](./src/main/kotlin/com/fResult/http/customers/CustomerRestController.kt)
    - Implementing reactive endpoints returning Mono/Flux with proper error handling
    - Serving templated views with [`CustomerViewController`](./src/main/kotlin/com/fResult/http/customers/CustomerViewController.kt) and [`TickerSseController`](./src/main/kotlin/com/fResult/http/views/TickerSseController.kt)
- Functional Endpoints
    - Simple endpoints with `RouterFunction<ServerResponse>` in [`SimpleFunctionalEndpointConfiguration`](./src/main/kotlin/com/fResult/http/routes/SimpleFunctionalEndpointConfiguration.kt)
    - Nested routing with path and media type predicates in [`NestedFunctionalEndpointConfiguration`](./src/main/kotlin/com/fResult/http/routes/NestedFunctionalEndpointConfiguration.kt)
    - Separating handler logic with dedicated handler classes like [`CustomerHandler`](./src/main/kotlin/com/fResult/http/customers/CustomerHandler.kt) and [`NestedHandler`](./src/main/kotlin/com/fResult/http/routes/NestedHandler.kt)
    - Custom route predicates with case-insensitive matching in [`CaseInsensitiveRequestPredicates`](./src/main/kotlin/com/fResult/http/routes/CaseInsensitiveRequestPredicates.kt) and usage in [`CustomRoutePredicatesConfiguration`](./src/main/kotlin/com/fResult/http/routes/CustomRoutePredicatesConfiguration.kt)
- Web Filters
    - URL normalization with [`LowerCaseWebFilter`](./src/main/kotlin/com/fResult/http/filters/LowerCaseWebFilter.kt)
    - Filter chain demonstration with before/after hooks in [`LowerCaseWebConfiguration`](./src/main/kotlin/com/fResult/http/filters/LowerCaseWebConfiguration.kt)
    - Global error handling with [`ErrorHandlingRouteConfiguration`](./src/main/kotlin/com/fResult/http/filters/ErrorHandlingRouteConfiguration.kt)
- Server-Sent Events (SSE)
    - Implementing SSE endpoints with [`SseRouteConfiguration`](./src/main/kotlin/com/fResult/sse/SseRouteConfiguration.kt)
    - Client-side SSE consumption with JavaScript
    - Data-driven UI updates with Thymeleaf fragments
- WebSocket Implementation
    - Basic echo service with [`EchoWebSocketConfiguration`](./src/main/kotlin/com/fResult/ws/echo/EchoWebSocketConfiguration.kt)
    - Demo chat application with [`ChatWebSocketConfiguration`](./src/main/kotlin/com/fResult/ws/chat/ChatWebSocketConfiguration.kt)
    - Managing WebSocket sessions and broadcasting messages
    - Bidirectional communication between server and clients
- Cross-Origin Configuration
    - Global CORS settings with [`CorsGlobalConfiguration`](./src/main/kotlin/com/fResult/common/CorsGlobalConfiguration.kt)
    - Configuring allowed origins, methods, headers, and credentials

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:07-http:webflux:clean\
            :kotlin:07-http:webflux:bootJar
```

### Running Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:07-http:webflux:bootRun
```
