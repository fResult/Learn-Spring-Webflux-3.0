# Spring RSocket

[← Back to \[08 RSocket\]'s README](../README.md)

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
