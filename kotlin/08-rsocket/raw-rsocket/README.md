# 08 RSocket - Raw RSocket Module (Kotlin)

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- Request/Response Model
  - xxxxxxxxxx
  - yyyyyyyyyy

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

[← Back to \[08 RSocket\]'s README](../README.md)
