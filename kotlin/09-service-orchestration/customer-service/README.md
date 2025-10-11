# Customer Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

## Implementation Details

- xxxxx
- yyyyy

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
