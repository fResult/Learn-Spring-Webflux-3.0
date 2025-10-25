# Client

[← Back to \[09 Service Orchestration\]'s README](../README.md)

**Client** shows different patterns for calling and working with multiple services.\
This module has examples for hedging, scatter-gather, and resilience patterns using services like [Customer Service](../customer-service/README.md), [Profile Service](../profile-service/README.md), [Order Service](../order-service/README.md), [Slow Service](../slow-service/README.md), and [Error Service](../error-service/README.md).

## Implementation Details

- Scatter-Gather pattern - calls `Customer Service`, `Profile Service`, and `Order Service` at the same time and combines results

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:clean \
            :kotlin:09-service-orchestration:client:build
```

### Running Application

#### Scatter Gather Script

First, make sure that the [Eureka server](../eureka-service/README.md#running-application) and [Customer Service](../customer-service/README.md#running-application) are up and running.

Then, run the [`ScatterGatherApplication`](src/main/kotlin/com/fResult/orchestration/scatterGather/ScatterGatherApplication.kt):

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:bootScatterGatherClient
```

#### Reactor Basic Script

First, make sure that the [Eureka server](../eureka-service/README.md#running-application) and [Customer Service](../customer-service/README.md#running-application) are up and running.

Then, run the [`BasicReactorApplication`](src/main/kotlin/com/fResult/orchestration/reactor/BasicApplication.kt):

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:bootReactorBasicClient
```

#### Resilient4j Script

First, make sure that the [Eureka server](../eureka-service/README.md#running-application) and [Error Service](../error-service/README.md#running-application) are up and running.

Then, run the [`ResilientClientApplication`](src/main/kotlin/com/fResult/orchestration/resilience4j/ResilientClientApplication.kt):

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:bootResilience4jClient
```

To run with `retry` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=retry ./gradlew :kotlin:09-service-orchestration:client:bootResilience4jClient
```

To run with `circuit-breaker` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=circuit-breaker ./gradlew :kotlin:09-service-orchestration:client:bootResilience4jClient
```

To run with `rate-limiter` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=rate-limiter ./gradlew :kotlin:09-service-orchestration:client:bootResilience4jClient
```

To run with `bulkhead` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=bulkhead ./gradlew :kotlin:09-service-orchestration:client:bootResilience4jClient
```

#### Hedging Script

First, make sure that the [Eureka server](../eureka-service/README.md#running-application) and two instances of [Slow Service](../slow-service/README.md#running-application) are up and running.\

Then, run few instances of Slow Service on different ports:

1) Open a terminal and run 10 seconds delayed instance:

    ```bash
    kotlin/09-service-orchestration/slow-service/run_slow.sh
    ```

2) Open another terminal and run another 10 seconds delayed instance:

    ```bash
    kotlin/09-service-orchestration/slow-service/run_slow.sh
    ```

3) Open more terminals and run 0 seconds delayed instances:

    ```bash
    kotlin/09-service-orchestration/slow-service/run_fast.sh
    ```

4) Finally, open one more terminal and run another 0 seconds delayed instance:

    ```bash
    kotlin/09-service-orchestration/slow-service/run_fast.sh
    ```

Then, run the [`HedgingApplication`](src/main/kotlin/com/fResult/orchestration/hedging/HedgingApplication.kt):

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=hedging ./gradlew :kotlin:09-service-orchestration:client:bootHedgingClient
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
