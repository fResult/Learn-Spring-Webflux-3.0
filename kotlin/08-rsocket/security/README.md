# Security

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- xxxxx
- yyyyy

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:security:clean \
            :kotlin:08-rsocket:security:build
```

### Running Application

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:security:bootServiceRun
```

Then, in a separate terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:08-rsocket:security:bootClientRun
```

[← Back to \[08 RSocket\]'s README](../README.md)

