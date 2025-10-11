# 09 Service Orchestration (Kotlin)

[← Back to Main README](../../README.md)

This module is based on concepts from the [orchestration repository](https://github.com/reactive-spring-book/orchestration) with my adaptations.

## Available Scripts

### Building Applications

- To build **Eureka service**, see [eureka-service/README.md](./eureka-service/README.md#building-application)
- To build **Customer service**, see [customer-service/README.md](./customer-service/README.md#building-application)
- To build **Profile service**, see [profile-service/README.md](./profile-service/README.md#building-application)
- To build **Order service**, see [order-service/README.md](./order-service/README.md#building-application)
- To build **Slow service**, see [slow-service/README.md](./slow-service/README.md#building-application)

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:eureka-service:clean \
            :kotlin:09-service-orchestration:eureka-service:build
```

### Running Applications

To run an Eureka server:

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:eureka-service:bootRun
```

To run a Profile service:

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:profile-service:bootRun
```

## Some Interesting Use Cases

### Hedging

1. Run `eureka-service`
2. Run `slow-service` and specify an environment variable `SLOW_SERVICE_DELAY=0`.\
    Let call this `fast-slow`.
3. Run `slow-service` and specify an environment variable `SLOW_SERVICE_DELAY=10`.\
    Let call this `slow-slow`.
4. Run `HedgingApplication` in `client`

### Scatter-Gather

1. Run `eureka-service`, `profile-service`, `order-service`, and `customer-service`
2. Run `ScatterGatherApplication` in `client`

### Resilience4j

1. Run `eureka-service`
2. Run `error-service`
3. Run `ResilienceApplication` in client.\
    There are four different demos in the same package as the main class, so be sure to note the profile of the demo that you want to run.\
    Here are the profile names: `bulkhead`, `circuit-breaker`, `rate-limiter`, and `retry`.

### Gateway

- \<TBD\>

[← Back to Main README](../../README.md)
