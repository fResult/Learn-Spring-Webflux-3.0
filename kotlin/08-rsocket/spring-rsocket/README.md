# Spring RSocket

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- Spring RSocket Integration
  - Spring Boot RSocket (via `spring-boot-starter-rsocket` dependency) 
    - Server port & transport via `spring.rsocket.server.port` ([`application-service.yml`](./src/main/resources/application-service.yml))
    - Embedded server port & transport configured in [`application-service.yml`](./src/main/resources/application-service.yml)  
    - Client beans (`RSocketRequester`) defined in `RSocketConfiguration.kt` for each application (request/response, channel, fire-and-forget, and so on)
  - `@MessageMapping`-driven endpoints
  - Automatic payload serialization with Jackson
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
