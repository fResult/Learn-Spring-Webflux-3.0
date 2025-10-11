# Profile Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

## Implementation Details

- xxxxx
- yyyyy

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:profile-service:clean \
            :kotlin:09-service-orchestration:profile-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:profile-service:bootRun
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance PROFILE-SERVICE/profile-service:e409c25641fcbcc09cd241c1cdbe89d2 with status UP (replication=true)service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
