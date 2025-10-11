# Slow Service

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)

## Implementation Details

- xxxxx
- yyyyy

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:slow-service:clean \
            :kotlin:09-service-orchestration:slow-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:slow-service:bootRun
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance SLOW-SERVICE/slow-service:01a578039aaf66bb87cec9044db59434 with status UP (replication=true)in:09-service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../customer-service/README.md)
