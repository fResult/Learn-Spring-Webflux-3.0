# Order Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

**Order Service** provides all the orders that belong to `Customer`.\
This service works with [Customer Service](../customer-service/README.md) in a one-to-many relationship with each `Customer` able to have many `Order` instances.

## Implementation Details

- xxxxx
- yyyyy

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:order-service:clean \
            :kotlin:09-service-orchestration:order-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:order-service:bootRun
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance ORDER-SERVICE/order-service:e975b630c01d096d7770ea0f6c1813bb with status UP (replication=true):09-service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
