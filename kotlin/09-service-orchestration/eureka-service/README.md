# Eureka Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

## Implementation Details

- **Eureka Server Configuration**
    - Spring Boot auto-configuration via `@EnableEurekaServer` annotation on main application class [`EurekaServiceApplication`](./src/main/kotlin/com/fResult/orchestration/EurekaServiceApplication.kt)
    - Standalone mode configuration in [`application.yml`](./src/main/resources/application.yml):
        - `fetch-registry: false` — prevents server from attempting to fetch registry from other Eureka instances
        - `register-with-eureka: false` — prevents server from registering itself as a client
    - Server running on port 8761 (default Eureka port)
    - Application name set to `eureka-service` for identification

- **Service Registration (Client-Side)**
    - Client services configured to register with Eureka server
    - Dynamic port assignment via `server.port: 0` in client applications
    - Unique instance ID generation using `${spring.application.name}:${spring.application.instance_id:${random.value}}` pattern
    - Custom configuration properties for service-specific settings (e.g., `slow-service.delay-in-seconds`)

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:eureka-service:clean \
            :kotlin:09-service-orchestration:eureka-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
