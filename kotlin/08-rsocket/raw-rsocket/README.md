# 08 RSocket - Raw RSocket Module (Kotlin)

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- Raw RSocket Client/Server Implementation
  - Low-level io.rsocket API usage (`RSocketConnector`/`RSocketServer`), no Spring abstractions
  - Manual TCP transport setup via `TcpClientTransport`/`TcpServerTransport`
  - Custom payload encoding/decoding with `DefaultPayload` and [`EncodingUtils`](../common/src/main/kotlin/com/fResult/rsocket/EncodingUtils.kt)
  - Manual acceptor/handler implementations for each interaction model
- Request/Response Pattern
  - Blocking RSocket request-response client repeating 10 times [`RequestResponseClient`](./src/main/kotlin/com/fResult/rsocket/requestResponse/client/RequestResponseClient.kt)
  - RSocket server handling request-response interactions  [`RequestResponseService`](./src/main/kotlin/com/fResult/rsocket/requestResponse/service/RequestResponseService.kt)
- Fire-and-Forget Pattern
  - RSocket fire-and-forget client sending a single payload [`FireAndForgetClient`](./src/main/kotlin/com/fResult/rsocket/fireAndForget/client/FireAndForgetClient.kt)
  - RSocket server handling fire-and-forget messages with no response [`FireAndForgetService`](./src/main/kotlin/com/fResult/rsocket/fireAndForget/service/FireAndForgetService.kt)
- Channel (Bidirectional Streaming) Pattern
  - RSocket channel client sending "Hello #n" pulses and logging 10 replies [`ChannelClient`](./src/main/kotlin/com/fResult/rsocket/channel/client/ChannelClient.kt)
  - RSocket channel server echoing incoming payloads back as a stream [`ChannelService`](./src/main/kotlin/com/fResult/rsocket/channel/service/ChannelService.kt)
- Bidirectional Communication
  - RSocket client with server-initiated health stream (acceptor + requestStream) [`BidirectionalClient`](./src/main/kotlin/com/fResult/rsocket/bidirectional/client/BidirectionalClient.kt), [BidirectionalClientLauncher](./src/main/kotlin/com/fResult/rsocket/bidirectional/client/BidirectionalClientLauncher.kt)
  - RSocket service generating greeting stream until client stops [`BidirectionalService`](./src/main/kotlin/com/fResult/rsocket/bidirectional/service/BidirectionalService.kt)
  - Shared domain model for health signaling [`ClientHealthState`](./src/main/kotlin/com/fResult/rsocket/bidirectional/ClientHealthState.kt)
- Metadata Push
  - RSocket metadata-push client sending update payload [`MetadataPushClient`](./src/main/kotlin/com/fResult/rsocket/metadata/client/MetadataClient.kt)
  - RSocket server handling metadata-push events and decoding headers  [`MetadataPushService`](./src/main/kotlin/com/fResult/rsocket/metadata/service/MetadataService.kt)

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:clean\
              :kotlin:08-rsocket:raw-rsocket:build
```

### Running Application

#### Request/Response Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootRequestResponseService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootRequestResponseClient
```

#### Fire-and-Forget Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootFireAndForgetService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootFireAndForgetClient
```

#### Channel Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootChannelService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootChannelClient
```

#### Bidirectional Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootBidirectionalService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootBidirectionalClient
```

#### Metadata Push Scripts

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootMetadataPushService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:08-rsocket:raw-rsocket:bootMetadataPushClient
```

[← Back to \[08 RSocket\]'s README](../README.md)
