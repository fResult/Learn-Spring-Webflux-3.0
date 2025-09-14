# 05 Reactor (Kotlin)

[← Back to Main README](../../README.md)

This module is based on concepts from the [reactor repository](https://github.com/reactive-spring-book/reactor) with my adaptations.

This module demonstrates core reactive programming concepts using Project Reactor in Kotlin, including:

- Reactive streams with `Flux` and `Mono`
- Reactive operators like `map`, `flatMap`, and `filter`
- Error handling in reactive streams
- Testing reactive streams with `StepVerifier`
- Backpressure management
- Hot and cold streams

## Implementation Details

- Implemented reactive programming concepts using Kotlin and Project Reactor
- Used thread-safe counters and decorators to monitor thread execution
- Created custom schedulers and thread factories for better control over execution context
- Applied reactive patterns like `subscribeOn` for controlling execution threads
- Implemented proper error handling and backpressure management
- Used `StepVerifier` for testing asynchronous reactive streams
- Created proxies with method interception for monitoring scheduler execution

## Differences from Original

- Replaced `EmitterProcessor` with `Sinks.many().multicast().onBackpressureBuffer()` to address deprecation
- Replaced `ReplayProcessor` with `Sinks.many().replay().limit()` as a modern alternative

## Running the Project

### Build

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :kotlin:05-reactor:clean \
             :kotlin:05-reactor:build
```

### Run App

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :kotlin:05-reactor:bootRun
```

### Test

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :kotlin:05-reactor:test
```

## See Also

For the Java implementation of this module, see the [Java version](../../java/05-reactor).

[← Back to Main README](../../README.md)
