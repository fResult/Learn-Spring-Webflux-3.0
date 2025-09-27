# 08 RSocket (Kotlin)

[← Back to Main README](../../README.md)

This module is based on concepts from the [rsocket repository](https://github.com/reactive-spring-book/rsocket) with my adaptations.

- [Raw RSocket Module](./raw-rsocket/README.md)
- [Spring RSocket Module](./spring-rsocket/README.md)

## Table 1. Message Exchange Pattern Formulations with the RSocketRequester

| Pattern          |   In    |    Out     |
|------------------|:-------:|:----------:|
| Request/Resposne | Mono<T> |  Mono<T>   |
| Stream           | Mono<T> |  Flux<T>   |
| Fire and Forget  | Mono<T> | Mono<Void> |
| Channel          | Flux<T> |  Flux<T>   |

[← Back to Main README](../../README.md)
