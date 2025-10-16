# Client

[← Back to \[09 Service Orchestration\]'s README](../README.md)

**Client** shows different patterns for calling and working with multiple services.\
This module has examples for hedging, scatter-gather, and resilience patterns using services like [Customer Service](../customer-service/README.md), [Profile Service](../profile-service/README.md), [Order Service](../order-service/README.md), [Slow Service](../slow-service/README.md), and [Error Service](../error-service/README.md).

## Implementation Details

- xxxxx
- yyyyy

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:clean \
            :kotlin:09-service-orchestration:client:build
```

### Running Application

First, make sure that the [Eureka server](../eureka-service/README.md#running-application) and [Customer Service](../customer-service/README.md#running-application) are up and running.

Then, run the [`ScatterGatherApplication`](src/main/kotlin/com/fResult/orchestration/scatterGather/ScatterGatherApplication.kt):

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:bootScatterGatherClient
```
