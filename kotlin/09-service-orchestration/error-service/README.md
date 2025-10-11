# Error Service

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)

## Implementation Details

- xxxxx
- yyyyy

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
