# 09 Service Orchestration (Kotlin)

[← Back to Main README](../../README.md)

This module is based on concepts from the [orchestration repository](https://github.com/reactive-spring-book/orchestration) with my adaptations.

## Available Scripts

### Building Applications

- To build **Eureka service**, see [eureka-service/README.md](./eureka-service/README.md#building-application)
- To build **Customer service**, see [customer-service/README.md](./customer-service/README.md#building-application)
- To build **Profile service**, see [profile-service/README.md](./profile-service/README.md#building-application)

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:eureka-service:clean \
            :kotlin:09-service-orchestration:eureka-service:build
```

[← Back to Main README](../../README.md)
