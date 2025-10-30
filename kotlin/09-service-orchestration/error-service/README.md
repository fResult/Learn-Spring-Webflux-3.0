# Error Service

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)

**Error Service** offers endpoints that fail in particular ways, also for demonstration purposes.

**Error Service** returns different types of errors.\
This service shows how to handle services that fail in different ways.

**Error Service** offers endpoints that fail in particular ways, also for demonstration purposes.

## Implementation Details

- Provides multiple REST endpoints for testing different error scenarios ([`ErrorRestController.kt`](src/main/kotlin/com/fResult/orchestration/ErrorRestController.kt))
- **`/ok` endpoint**: Always returns successful response with greeting message
    - Supports `uid` query parameter to track client request counts
    - Returns greeting with attempt count and server port
- **`/retry` endpoint**: Fails first 2 attempts, succeeds on 3rd attempt
    - Uses `ConcurrentHashMap<String, AtomicInteger>` to track per-client attempt counts
    - Returns `IllegalArgumentException` for first 2 attempts to trigger retry logic
    - Succeeds with greeting response after 2 failed attempts
- **`/circuit-breaker` endpoint**: Always fails to trigger circuit breaker opening
    - Returns `IllegalArgumentException` for all requests
    - Used to test circuit breaker failure threshold and open state
- Tracks client request counts using `uid` parameter for stateful error simulation ([`ErrorRestController.kt`](src/main/kotlin/com/fResult/orchestration/ErrorRestController.kt))
- Global exception handler converts exceptions to HTTP `400 Bad Request` with `ProblemDetail` response ([`GlobalExceptionHandler.kt`](src/main/kotlin/com/fResult/orchestration/GlobalExceptionHandler.kt))
- Registers with Eureka server using dynamic instance ID ([`application.yml`](src/main/resources/application.yml))
- Uses random port assignment (`server.port: 0`) for multiple instances

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:error-service:clean \
            :kotlin:09-service-orchestration:error-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:error-service:bootRun
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance ORDER-SERVICE/order-service:e975b630c01d096d7770ea0f6c1813bb with status UP (replication=true):09-service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)
