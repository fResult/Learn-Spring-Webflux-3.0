# Slow Service

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)

**Slow Service** returns responses with delays.\
This service shows how to handle services that take a long time to respond.

## Implementation Details

- Provides REST endpoint `/greetings` for retrieving greeting messages ([`SlowRestController.kt`](src/main/kotlin/com/fResult/orchestration/SlowRestController.kt))
- Supports optional `name` query parameter (default: "World") for personalized greetings
- Returns `Mono<GreetingResponse>` for reactive processing
- **Configurable delay** using `slow-service.delay-in-seconds` property in [`application.yml`](src/main/resources/application.yml) (default: 0 seconds)
- Can override delay via `SLOW_SERVICE_DELAY` environment variable for testing different latency scenarios
- Uses `delaySubscription()` to simulate network latency before returning response
- Logs request start time, delay duration, and finish time for tracking response performance
- Greeting response includes server port and timestamp information for debugging hedging behavior
- Registers with Eureka server using dynamic instance ID ([`application.yml`](src/main/resources/application.yml))
- Uses random port assignment (`server.port: 0`) for running multiple instances with different delays
- Logs server port on startup using `WebServerInitializedEvent` listener
- Configuration properties managed via `@ConfigurationProperties` ([`SlowServiceProperties.kt`](src/main/kotlin/com/fResult/orchestration/SlowServiceProperties.kt))

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:slow-service:clean \
            :kotlin:09-service-orchestration:slow-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:slow-service:bootRun
```

To simulate slow service for 5 seconds, you can use `slow-service.delay-in-seconds` property:

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:slow-service:bootRun \
    --args='--slow-service.delay-in-seconds=5'
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance SLOW-SERVICE/slow-service:01a578039aaf66bb87cec9044db59434 with status UP (replication=true)in:09-service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)
