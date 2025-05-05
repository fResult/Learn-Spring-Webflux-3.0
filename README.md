# Learn Spring Webflux 3.0

This repository contains my hands-on exercises while learning Spring WebFlux 3.0 following the [Reactive Spring](https://leanpub.com/reactive-spring) book.

## Prerequisites
- JDK 24 (for Java implementations)
- JDK 23 (for Kotlin implementations)
- Gradle 8.14

## Project Structure

The project is organized into modules demonstrating different aspects of Spring WebFlux:
- `03-bootstrap`: Implementation of core Spring concepts including transaction management

Each module is implemented in both [Java](./java) and [Kotlin](./kotlin) to compare approaches.

## My Summary

### What I Learned From Reading This Book

- Appreciated how Spring Boot significantly simplifies transaction management through annotations like `@Transactional`, eliminating boilerplate code and automatically handling method inheritance

### Things I Did Different and Learned Further

- Used Gradle Kotlin DSL instead of Maven for build configuration
- Implemented parallel Kotlin versions of all Java examples
- Updated to Spring Boot 3.4.5 while the book uses an older Spring Boot version 2.5.0

## Running the Project

```bash
# Run Java version
./gradlew :java:03-bootstrap:bootRun

# Run Kotlin version
./gradlew :kotlin:03-bootstrap:bootRun

# Run all tests
./gradlew test
```
