# 05 Reactor (Kotlin)

[← Back to Main README](../../README.md)

This module is based on concepts from the [reactor repository](https://github.com/reactive-spring-book/reactor) with my adaptations.

## Implementation Details

- xxxxxxxxxxxxx
- yyyyyyyyyyyyy

## Differences from Original

- Use `Sinks.many().multicast().onBackpressureBuffer` instead of `EmitterProcessor` as a replacement for `FluxSink` in the original code because `EmitterProcessor` is deprecated.
- Use `Sinks.many().replay().limit()` instead of `ReplayProcessor` as a replacement for `FluxSink` in the original code because `ReplayProcessor` (with buffer) is deprecated.

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
   ./gradlew :kotlin:05-reactor:runApp
```

### Test

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :kotlin:05-reactor:test
```

