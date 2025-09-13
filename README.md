# Learn Spring Webflux 3.0

<a href="https://leanpub.com/reactive-spring"><img src="https://d2sofvawe08yqg.cloudfront.net/reactive-spring/s_hero?1620596567" alt="" height="350px" align="right"></a>

This repository contains my hands-on exercises while learning Spring WebFlux 3.0 following the [Reactive Spring](https://leanpub.com/reactive-spring) book and [these repositories](https://github.com/Reactive-Spring-Book).

## Prerequisites

- JDK 24 (for Java implementations)
- JDK 23 (for Kotlin implementations)
- Gradle 8.14

## Project Structure

The project is organized into modules demonstrating different aspects of Spring WebFlux:

Each module is implemented in both [Java](./java) and [Kotlin](./kotlin) to compare approaches.

- `03-bootstrap`: Implementation of core Spring concepts including dependency injection, configuration, application context, and transaction management fundamental
   - [Java Implementation](./java/03-bootstrap)
   - [Kotlin Implementation](./kotlin/03-bootstrap)
- `04-input-and-output`: xxxxxxxxxxxxxxxxx
   - [Java Implementation](./java/04-input-and-output)
   - [Kotlin Implementation](https://github.com/fResult/Learn-Spring-Webflux-3.0/tree/04_input-and-output/kotlin/04-input-and-output)
- `05-reactor`: Implementation of reactive programming concepts using Project Reactor with Mono/Flux operators, error handling, backpressure management and testing strategies
   - [Java Implementation](./java/05-reactor)
   - [Kotlin Implementation](./kotlin/05-reactor)
- `06-data-access`: Implementation of reactive database access using Spring Data R2DBC with schema definitions, repository patterns, and transaction management
   - Java Implementation (No implementation)
   - [Kotlin Implementation](./kotlin/06-data-access)
- `07-http`: Implementation of reactive HTTP applications featuring WebClient for clients, WebFlux for servers, security integration, and comparison with servlet-based approaches
   - Java Implementation (No implementation)
   - [Kotlin Implementation](./kotlin/07-http)

## My Summary

### What I Learned From Reading This Book

- Appreciated how Spring Boot speeds up development through starter packages, auto-configuration, and easy transaction management
- Understood the benefits of non-blocking I/O for scalable applications
- Understood reactive programming principles and how they differ from imperative programming
- Mastered Project Reactor's core (`Mono` and `Flux`) and how they enable composition of asynchronous operations
- Learned reactive database access patterns with Spring Data R2DBC replacing traditional JDBC

### Things I Did Different and Learned Further

- Used Gradle Kotlin DSL instead of Maven for build configuration
- Implemented parallel Kotlin versions of Java examples
- Updated to Spring Boot 3.5.x while the book uses an older Spring Boot version 2.5.0
- Adopted a monorepo approach with [Gradle Multi-project Builds][gradle-multiproject] and [Gradle Composite Builds][gradle-composite-builds] to manage both Java and Kotlin implementations in a single repository
- Implemented database profile switching between R2DBC, MongoDB, and other providers

<!-- References -->
[gradle-multiproject]: https://docs.gradle.org/current/userguide/intro_multi_project_builds.html
[gradle-composite-builds]: https://docs.gradle.org/current/userguide/composite_builds.html
