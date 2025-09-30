# Spring RSocket

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- Spring RSocket Integration
  - Spring Boot auto-configuration via `spring-boot-starter-rsocket` (all interaction models)
    - Server & client settings from `spring.rsocket.*` ([`application-service.yml`](./src/main/resources/application-service.yml))
    - Client beans (`RSocketRequester`) defined in `RSocketConfiguration` class for each application (request/response, channel, fire-and-forget, and so on)
    - Jackson JSON serialization enabled automatically
  - `@MessageMapping`-driven endpoints
- Request/Response Endpoints
  - `RSocketRequester` bean configuration [`RSocketConfiguration `](./src/main/kotlin/com/fResult/rsocket/requestResponse/client/RSocketConfiguration.kt)
  - Client invoking greeting route and retrieving `Mono<String>` with retry logic [`RequestResponseClient`](./src/main/kotlin/com/fResult/rsocket/requestResponse/client/RequestResponseClient.kt)
  - Controller handling greeting messages, logging headers and returning `Mono<String>` [`GreetingController`](./src/main/kotlin/com/fResult/rsocket/requestResponse/service/GreetingController.kt)
- Channel (Streaming) Endpoints
  - Bean setup for `RSocketRequester` [`RSocketConfiguration`](./src/main/kotlin/com/fResult/rsocket/channel/client/RSocketConfiguration.kt)
  - Full-duplex channel client sending "Ping #n" with retry and stream callbacks [`ChannelClient`](./src/main/kotlin/com/fResult/rsocket/channel/client/ChannelClient.kt)
  - Reactive controller for request-channel endpoint with ping → pong transformation and logging [`PongController`](./src/main/kotlin/com/fResult/rsocket/channel/service/PongController.kt)
- Fire-and-Forget Endpoints
  - `RSocketRequester` bean configuration [`RSocketConfiguration`](./src/main/kotlin/com/fResult/rsocket/fireAndForget/client/RSocketConfiguration.kt)
  - Controller handling fire-and-forget “greeting” commands, logging incoming names [`GreetingController`](./src/main/kotlin/com/fResult/rsocket/fireAndForget/service/GreetingController.kt)
  - Client sending 10 fire-and-forget greeting commands with `.send()`, retry logic and success/failure callbacks [`FireAndForgetClient`](./src/main/kotlin/com/fResult/rsocket/fireAndForget/client/FireAndForgetClient.kt)
- Bidirectional Communication
  - Server-side greetings endpoint with `@MessageMapping("greetings")`, streaming greetings until the client signals STOP via health notifications [`GreetingController`](./src/main/kotlin/com/fResult/rsocket/bidirectional/service/GreetingController.kt)
  - Client-side `health` endpoint providing a continuous Flux of `ClientHealthState` for server-driven stop control [`HealthController`](./src/main/kotlin/com/fResult/rsocket/bidirectional/client/HealthController.kt)
  - `RSocketRequester` bean with embedded `SocketAcceptor` to wire health handler and establish bidirectional connection [`RSocketConfiguration`](./src/main/kotlin/com/fResult/rsocket/bidirectional/client/RSocketConfiguration.kt)
  - Bidirectional client invoking greetings route to retrieve `Flux<GreetingResponse>`, plus a multi-client launcher with retry/backoff and lifecycle callbacks
    [`BidirectionalClient`](./src/main/kotlin/com/fResult/rsocket/bidirectional/client/BidirectionalClient.kt),
    [`BidirectionalClientLauncher`](./src/main/kotlin/com/fResult/rsocket/bidirectional/client/BidirectionalClientLauncher.kt)
- Setup Connections
  - Client RSocketRequester bean configured with `setupData`, `setupRoute` and a fixed-delay reconnect strategy [`RSocketConfiguration`](./src/main/kotlin/com/fResult/rsocket/setup/client/RSocketConfiguration.kt)
  - Server-side setup handler using `@ConnectMapping("setup")` to log the incoming setup payload and headers [`SetupController`](./src/main/kotlin/com/fResult/rsocket/setup/service/SetupController.kt)
  - Greeting endpoint with a destination variable (`greetings.{name}`) returning a `Mono<String>` [`SetupController`](./src/main/kotlin/com/fResult/rsocket/setup/service/SetupController.kt)
  - ApplicationRunner demonstrating a request to `greetings.{name}` and subscribing to the `Mono<String>` response [`SetupClientConfiguration`](./src/main/kotlin/com/fResult/rsocket/setup/client/SetupClientConfiguration.kt)
- Routing Endpoints
  - `RSocketRequester` bean configuration [`RSocketConfiguration`](./src/main/kotlin/com/fResult/rsocket/routing/client/RSocketConfiguration.kt)
  - Server-side controller defining two routes ([`CustomerController`](./src/main/kotlin/com/fResult/rsocket/routing/service/RoutingController.kt)):
    - `@MessageMapping("customers")` → `Flux<Customer>`
    - `@MessageMapping("customers.{id}")` → `Mono<Customer>` by ID
  - Client invoking both routes via `.route(...)`, retrieving a `Flux<Customer>` for all and `Mono<Customer>` for a single ID, with onNext/onError/onComplete callbacks [`RoutingClient`](./src/main/kotlin/com/fResult/rsocket/routing/client/RoutingClient.kt)
  - Domain model for customer data [`Customer`](./src/main/kotlin/com/fResult/rsocket/routing/Customer.kt)

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:clean \
            :kotlin:08-rsocket:spring-rsocket:build
```

### Running Application

#### Request/Response Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootRequestResponseService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootRequestResponseClient
```

#### Channel (Stream) Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootChannelService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootChannelClient
```

#### Fire-and-Forget Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootFireAndForgetService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootFireAndForgetClient
```

#### Bidirectional Communication Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootBidirectionalService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootBidirectionalClient
```

#### Setup Connections Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootSetupConnectionsService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootSetupConnectionsClient
```

#### Routing Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootRoutingService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:spring-rsocket:bootRoutingClient
```
