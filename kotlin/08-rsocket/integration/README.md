# Integration

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- Reactive greeting controller streaming 10 responses with delay and logging [`GreetingController`](./src/main/kotlin/com/fResult/rsocket/service/GreetingController.kt)
- RSocket client connector bean configured with host, port and `RSocketStrategies` [`RSocketConnectionConfiguration`](./src/main/kotlin/com/fResult/rsocket/integration/RSocketConnectionConfiguration.kt)
- Reactive message channel bean for `IntegrationFlow` [`ChannelConfiguration`](./src/main/kotlin/com/fResult/rsocket/integration/ChannelConfiguration.kt)
- File inbound channel adapter watching `$HOME/in` directory with fixed-rate poller [`FileInboundConfiguration`](./src/main/kotlin/com/fResult/rsocket/integration/FileInboundConfiguration.kt)
- Integration flow on [`IntegrationFlowConfiguration`](./src/main/kotlin/com/fResult/rsocket/integration/IntegrationFlowConfiguration.kt) that:
  - Reads files → transforms to `String` → maps to [`GreetingRequest`](../common/src/main/kotlin/com/fResult/rsocket/dtos/GreetingRequest.kt)
  - Sends to RSocket "greetings" request-stream gateway and expects [`GreetingResponse`](../common/src/main/kotlin/com/fResult/rsocket/dtos/GreetingResponse.kt)
  - Splits the response `Flux`, routes through the message channel, and logs payloads & headers

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:integration:clean \
            :kotlin:08-rsocket:integration:build
```

### Running Application

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:integration:bootServiceRun
```

Then, in a separate terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:integration:bootIntegrationRun
```

To test client side, create text files in `$HOME/in` directory.

```shell
# Unix shell
mkdir -p $HOME/in
echo "Java" > $HOME/in/java.txt
echo "Kotlin" > $HOME/in/kotlin.txt
echo "Spring" > $HOME/in/spring.txt

# Windows PowerShell
mkdir $env:USERPROFILE\in
"Java" | Out-File -FilePath $env:USERPROFILE\in\java.txt
"Kotlin" | Out-File -FilePath $env:USERPROFILE\in\kotlin.txt
"Spring" | Out-File -FilePath $env:USERPROFILE\in\spring.txt
```

[← Back to \[08 RSocket\]'s README](../README.md)
