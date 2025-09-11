# 07 HTTP - Security Module (Kotlin)

[← Back to \[07 HTTP\]'s README](../README.md)

## Implementation Details

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:07-http:security:clean\
              :kotlin:07-http:security:build
```

### Running Application

First, run the service application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:07-http:security:bootService
```

Then, in another terminal, run the client application:

```shell
cd $(git rev-parse --show-toplevel) && \
    ./gradlew :kotlin:07-http:security:bootClient
```

[← Back to \[07 HTTP\]'s README](../README.md)
