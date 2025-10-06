# Integration

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- xxxxx
- yyyyy

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

[← Back to \[08 RSocket\]'s README](../README.md)
