# Security

[← Back to \[08 RSocket\]'s README](../README.md)

## Implementation Details

- Reactive in-memory user store with BCrypt-hashed passwords and roles [`ServiceSecurityConfig.authorization()`](./src/main/kotlin/com/fResult/rsocket/service/ServiceSecurityConfig.kt)
- RSocket simple authentication configured via `RSocketSecurity.simpleAuthentication()` → `PayloadSocketAcceptorInterceptor` [`ServiceSecurityConfig.authorization()`](./src/main/kotlin/com/fResult/rsocket/service/ServiceSecurityConfig.kt)
- `AuthenticationPrincipalArgumentResolver` registered in `RSocketMessageHandler` for injecting authenticated user [`ServiceSecurityConfig.rSocketMessageHandler()`](./src/main/kotlin/com/fResult/rsocket/service/ServiceSecurityConfig.kt)
- Secure streaming endpoint with `@MessageMapping("greetings")` using `@AuthenticationPrincipal` to obtain `UserDetails` [`GreetingController.greet()`](./src/main/kotlin/com/fResult/rsocket/service/GreetingController.kt)
- Client credentials defined as `UsernamePasswordMetadata` beans for users **"fResult"** and **"KornZilla"** [`ClientSecurityConfig`](./src/main/kotlin/com/fResult/rsocket/client/ClientSecurityConfig.kt)
- `RSocketRequester` bean configured with `setupMetadata(credentials, MESSAGE_RSOCKET_AUTHENTICATION)` and `SimpleAuthenticationEncoder` registered [`RSocketConfiguration`](./src/main/kotlin/com/fResult/rsocket/client/RSocketConfiguration.kt)
- Demonstrating two authenticated greeting streams by sending credentials metadata and consuming `Flux<GreetingResponse>` with retry/backoff [`SecurityClient`](./src/main/kotlin/com/fResult/rsocket/client/SecurityClient.kt)
- Service and client application bootstrap classes with profile "service" ([`SecurityApplication`](./src/main/kotlin/com/fResult/rsocket/service/SecurityApplication.kt)) for the server and a blocking main for the client ([`SecurityApplication`](./src/main/kotlin/com/fResult/rsocket/client/SecurityApplication.kt))

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

