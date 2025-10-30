# Customer Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

## Implementation Details

- Provides REST endpoint `/customers` for retrieving customer data ([`CustomerRestController.kt`](src/main/kotlin/com/fResult/orchestration/CustomerRestController.kt))
- Supports filtering by customer IDs using `ids` query parameter (e.g., `/customers?ids=1,2,3`)
- Supports configurable delay using `delay` query parameter for testing slow responses
- Returns `Flux<Customer>` stream for reactive processing
- Uses `delaySubscription()` to simulate network latency when `delay=true`
- Configurable delay duration via `delay` property in [`application.yml`](src/main/resources/application.yml) (default: 2000ms)
- Registers with Eureka server using dynamic instance ID ([`application.yml`](src/main/resources/application.yml))
- Uses random port assignment (`server.port: 0`) for multiple instances
- Stores customer data in-memory as `Map<Int, Customer>` ([`Customer.kt`](src/main/kotlin/com/fResult/orchestration/Customer.kt))

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:customer-service:clean \
            :kotlin:09-service-orchestration:customer-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:customer-service:bootRun
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance CUSTOMER-SERVICE/customer-service:fa9669d4a0348597d4b38baaa58173a6 with status UP (replication=true)rvice-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
