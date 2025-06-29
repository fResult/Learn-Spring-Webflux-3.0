# Learn Spring Webflux 3.0

This repository contains my hands-on exercises while learning Spring WebFlux 3.0 following the [Reactive Spring](https://leanpub.com/reactive-spring) book and [these repositories](https://github.com/Reactive-Spring-Book).

## Prerequisites
- JDK 24 (for Java implementations)
- JDK 23 (for Kotlin implementations)
- Gradle 8.14

## Project Structure

The project is organized into modules demonstrating different aspects of Spring WebFlux:

Each module is implemented in both [Java](./java) and [Kotlin](./kotlin) to compare approaches.

- `03-bootstrap`: Implementation of core Spring concepts including transaction management
   - [Java Implementation](./java/03-bootstrap)
   - [Kotlin Implementation](./kotlin/03-bootstrap)
- `04-input-and-output`: xxxxxxxxxxxxxxxxx
   - [Java Implementation](./java/04-input-and-output)
   - [Kotlin Implementation](https://github.com/fResult/Learn-Spring-Webflux-3.0/tree/04_input-and-output/kotlin/04-input-and-output)
- `05-reactor`: Implementation of reactive programming concepts using Project Reactor
   - [Java Implementation](./java/05-reactor)
   - [Kotlin Implementation](./kotlin/05-reactor)

## My Summary

### What I Learned From Reading This Book

- Appreciated how Spring Boot speeds up development through starter packages, auto-configuration, and easy transaction management
- Practiced Project Reactor's core abstractions (Mono and Flux) and how they enable composition of asynchronous operations

### Things I Did Different and Learned Further

- Used Gradle Kotlin DSL instead of Maven for build configuration
- Implemented parallel Kotlin versions of all Java examples
- Updated to Spring Boot 3.5.x while the book uses an older Spring Boot version 2.5.0
- Adopted a monorepo approach with [Gradle multi-project builds][gradle-multiproject] to manage both Java and Kotlin implementations in a single repository

<!-- References -->
[gradle-multiproject]: https://docs.gradle.org/current/userguide/intro_multi_project_builds.html
