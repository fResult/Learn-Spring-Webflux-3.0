# 05 Reactor (Java)

[← Back to Main README](../../README.md)

This module is based on concepts from the [reactor repository](https://github.com/reactive-spring-book/reactor) with my adaptations.

This module demonstrates core reactive programming concepts using Project Reactor in Java, including:

- Reactive streams with `Flux` and `Mono`
- Reactive operators like `map`, `flatMap`, and `filter`
- Error handling in reactive streams
- Testing reactive streams with `StepVerifier`
- Backpressure management
- Hot and cold streams

## Implementation Details

- Implemented reactive programming using Project Reactor's Mono and Flux types
- Created examples of transforming data streams with map, flatMap, and other operators
- Demonstrated error handling in reactive streams using onErrorResume and onErrorReturn
- Used StepVerifier for testing asynchronous reactive code
- Implemented custom Scheduler examples to control execution context
- Showed examples of backpressure handling in reactive streams
- Demonstrated how to combine multiple reactive streams

## Differences from Original

- Updated deprecated APIs to current Project Reactor best practices
- Replaced `EmitterProcessor` with `Sinks.many().multicast().onBackpressureBuffer()` to address deprecation
- Replaced `ReplayProcessor` with `Sinks.many().replay().limit()` as a modern alternative

## Running the Project
### Build

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :java:05-reactor:clean \
             :java:05-reactor:build
```

### Run App

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :java:05-reactor:bootRun
```

### Test

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :java:05-reactor:test
```

## See Also

For the Java implementation of this module, see the [Kotlin version](../../kotlin/05-reactor).

[← Back to Main README](../../README.md)
