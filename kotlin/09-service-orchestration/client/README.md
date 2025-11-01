# Client

[← Back to \[09 Service Orchestration\]'s README](../README.md)

**Client** shows different patterns for calling and working with multiple services.\
This module has examples for hedging, scatter-gather, and resilience patterns using services like [Customer Service](../customer-service/README.md), [Profile Service](../profile-service/README.md), [Order Service](../order-service/README.md), [Slow Service](../slow-service/README.md), and [Error Service](../error-service/README.md).

## Implementation Details

### Scatter-Gather Pattern

- Uses `CrmClient` for making reactive HTTP calls via `WebClient` to backend services ([`CrmClient.kt`](src/main/kotlin/com/fResult/orchestration/scatterGather/CrmClient.kt))
- **Batch-optimized endpoints** for customers and orders:
  - `getCustomers(ids)`: Fetches multiple customers in single request using `?ids=1,2,3` query parameter
  - `getCustomersOrders(customerIds)`: Fetches all orders for multiple customers using `?customer-ids=1,2,3`
  - Leverages database batch queries (e.g., SQL `WHERE customer_fk IN (...)`) to avoid unnecessary network roundtrips
- **N+1 Problem demonstration** with `getCustomerProfile(customerId)`:
  - Intentionally fetches profiles one-by-one to simulate worst-case ORM pattern (see *Reactive Spring* Chapter 12, Section 12.7)
  - Makes N separate HTTP calls for N customers (e.g., 100 requests for 100 customers)
  - Uses non-RESTful endpoint `GET /profiles?customer-id=<id>` (should be `GET /customers/:customerId/profile` per REST conventions)
  - **Mitigated by reactive programming**: Launches all requests in parallel using `flatMap()` instead of serial execution
  - Still wasteful of network resources but significantly faster than blocking/serial approach
  - **Production recommendation**: Use batch endpoint (e.g., `GET /profiles?customer-ids=1,2,3`) instead
- Orchestrates data aggregation in `ScatterGather` component using `Flux.zip()` and reactive pipelines ([`ScatterGather.kt`](src/main/kotlin/com/fResult/orchestration/scatterGather/ScatterGather.kt)):
  - Enriches each `Customer` with their `orders` (filtered from batch result) and `profile` (fetched individually)
  - Combines into `CustomerWithDetails` model containing customer, orders list, and profile ([`CustomerWithDetails.kt`](src/main/kotlin/com/fResult/orchestration/CustomerWithDetails.kt))
- Uses `TimerUtils.cache()` and `TimerUtils.monitor()` for performance measurement and logging
- Uses service discovery (`http://customer-service`, `http://order-service`, `http://profile-service`) for dynamic routing via Eureka

### Reactor Basic Patterns

- **Timeout**: Limits response time using `timeout()` operator ([`TimeoutClient.kt`](src/main/kotlin/com/fResult/orchestration/reactor/TimeoutClient.kt))
- **Retry**: Retries failed requests with fixed number of attempts using `retry()` ([`RetryClient.kt`](src/main/kotlin/com/fResult/orchestration/reactor/RetryClient.kt))
- **Retry with Backoff**: Retries with exponential backoff using `retryWhen(Retry.backoff())` ([`RetryWhenClient.kt`](src/main/kotlin/com/fResult/orchestration/reactor/RetryWhenClient.kt))
- **Degrading/Fallback**: Returns empty result on errors using `onErrorResume()` ([`DegradingClient.kt`](src/main/kotlin/com/fResult/orchestration/reactor/DegradingClient.kt))
- Uses `OrderClient` with service discovery for calling Order Service ([`OrderClient.kt`](src/main/kotlin/com/fResult/orchestration/reactor/OrderClient.kt))

### Resilience4j Patterns

- **Retry**: Retries failed requests with exponential backoff using `RetryOperator` ([`RetryClient.kt`](src/main/kotlin/com/fResult/orchestration/resilience4j/RetryClient.kt))
  - Configures max attempts (3) and wait duration (1 second with 2x multiplier)
  - Uses `transformDeferred(RetryOperator.of(retry))` for declarative retry
- **Circuit Breaker**: Opens circuit after failure threshold to stop calling failing service ([`CircuitBreakerClient.kt`](src/main/kotlin/com/fResult/orchestration/resilience4j/CircuitBreakerClient.kt))
  - Configures failure rate threshold (50%), sliding window size (5), and wait duration in open state (1 second)
  - Uses `transformDeferred(CircuitBreakerOperator.of(circuitBreaker))`
  - Handles `CallNotPermittedException` when circuit is open
- **Rate Limiter**: Limits number of requests per time period ([`RateLimiterClient.kt`](src/main/kotlin/com/fResult/orchestration/resilience4j/RateLimiterClient.kt))
  - Configures limit for period (2 calls) and refresh period (5 seconds)
  - Uses `transformDeferred(RateLimiterOperator.of(rateLimiter))`
  - Tracks successful and failed requests with `AtomicInteger`
- **Bulkhead**: Limits concurrent requests to prevent resource exhaustion ([`BulkheadClient.kt`](src/main/kotlin/com/fResult/orchestration/resilience4j/BulkheadClient.kt))
  - Configures max concurrent calls (half of available processors) and max wait duration (5ms)
  - Uses `transformDeferred(BulkheadOperator.of(bulkhead))`
  - Demonstrates rejection when bulkhead is full
- Uses `GreetingClientUtils` to call Error Service with common `WebClient` logic ([`GreetingClientUtils.kt`](src/main/kotlin/com/fResult/orchestration/resilience4j/GreetingClientUtils.kt))
- Profile-based activation for testing different resilience patterns ([`application.yml`](src/main/resources/application.yml))

### Hedging Pattern

- Calls multiple service instances concurrently and accepts the first successful response ([`HedgingFilterFunction.kt`](src/main/kotlin/com/fResult/orchestration/hedging/HedgingFilterFunction.kt))
- Discovers available service instances using `ReactiveDiscoveryClient` ([`HedgingConfiguration.kt`](src/main/kotlin/com/fResult/orchestration/hedging/config/HedgingConfiguration.kt))
- Shuffles and limits instances to `maxNodes` (default: 3) for hedging ([`HedgingLoadBalancerProperties.kt`](src/main/kotlin/com/fResult/orchestration/hedging/config/HedgingLoadBalancerProperties.kt))
- Sends requests to multiple instances using `Flux.firstWithSignal()` to get fastest response
- Configures 10 seconds timeout for hedging requests
- Cancels pending requests automatically when first response arrives
- Uses custom `ExchangeFilterFunction` to intercept and transform requests
- Resolves service URIs from discovered instances (`http://<host>:<port><path>`)
- **Hedging Client** (`@HedgingWebClient`): Uses custom hedging filter for service discovery with hedging strategy ([`HedgingConfiguration.kt`](src/main/kotlin/com/fResult/orchestration/hedging/config/HedgingConfiguration.kt))
- **Load-Balanced Client** (`@LoadBalancedWebClient`): Uses standard Spring Cloud LoadBalancer filter ([`LoadBalancedClientConfiguration.kt`](src/main/kotlin/com/fResult/orchestration/hedging/config/LoadBalancedClientConfiguration.kt))
- Uses `@Qualifier` annotations to differentiate between hedging and load-balanced clients ([`HedgingWebClient.kt`](src/main/kotlin/com/fResult/orchestration/hedging/qualifier/HedgingWebClient.kt), [`LoadBalancedWebClient.kt`](src/main/kotlin/com/fResult/orchestration/hedging/qualifier/LoadBalancedWebClient.kt))
- Demonstrates both hedging (for `Slow Service`) and load-balanced (for `Order Service`) calls ([`HedgingRequestLauncher.kt`](src/main/kotlin/com/fResult/orchestration/hedging/client/HedgingRequestLauncher.kt))

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

#### Gateway Script

First, make sure that the [Eureka server](../eureka-service/README.md#running-application) and required backend services are up and running.

Then, run the [`GatewayApplication`](src/main/kotlin/com/fResult/orchestration/gateway/ApiGatewayApplication.kt):

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:client:bootGatewayClient
```

To run with `routes-simple` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=routes-simple ./gradlew :kotlin:09-service-orchestration:client:bootGatewayClient
```

To run with `routes-predicate` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=routes-predicate ./gradlew :kotlin:09-service-orchestration:client:bootGatewayClient
```

To run with `routes-filter-simple` profile enabled, use the following command:

```bash
cd $(git rev-parse --show-toplevel) && \
  SPRING_PROFILES_ACTIVE=routes-filter-simple ./gradlew :kotlin:09-service-orchestration:client:bootGatewayClient
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
