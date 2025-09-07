# Webflux Module

## Implementation Details

- HTTP Controllers
    - Using `@RestController` and `@RequestMapping` with [`CustomerRestController`](./src/main/kotlin/com/fResult/http/customers/CustomerRestController.kt)
    - Implementing reactive endpoints returning Mono/Flux with proper error handling
    - Serving templated views with [`CustomerViewController`](./src/main/kotlin/com/fResult/http/customers/CustomerViewController.kt) and [`TickerSseController`](./src/main/kotlin/com/fResult/http/views/TickerSseController.kt)
## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :webflux:clean \
            :webflux:build
```

### Running Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew webflux:bootRun
```
