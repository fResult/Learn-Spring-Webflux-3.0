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
