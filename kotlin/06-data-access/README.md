# 06 Data Access (Kotlin)

> [!warning]
> This module is not completed, because of some issues with the R2DBC driver.\
> Please see the [POC R2DBC](https://github.com/fResult/poc-r2dbc) repository instead.

[← Back to Main README](../../README.md)

This module is based on concepts from the [data repository](https://github.com/reactive-spring-book/data) with my adaptations.

This module demonstrates core Spring Boot concepts in Kotlin including:

- Starter dependencies
- Auto-configuration
- Transaction management
- Profile-specific configurations
- Actuator endpoints (e.g. `/actuator/beans`, `/actuator/configprops`, `/actuator/conditions`, `/actuator/threaddump`)

## Implementation Details

- xxxxxxxxxxxx
- yyyyyyyyyyyy

## Running the Project

### Prerequisites

- Docker and Docker Compose installed

### Prepare Database

Ensure that the PostgreSQL database is running. You can use Docker Compose to start it:

```bash
cd $(git rev-parse --show-toplevel) && \
  docker-compose \
    -f kotlin/06-data-access/docker/compose.yml \
    --env-file kotlin/06-data-access/.env \
    up -d
```

In case you need to STOP the database, you can run:

```bash
cd $(git rev-parse --show-toplevel) && \
  docker-compose \
    -f kotlin/06-data-access/docker/compose.yml \
    --env-file kotlin/06-data-access/.env \
    down -v
```

### Build

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :kotlin:06-data-access:clean \
             :kotlin:06-data-access:build

```

### Run App

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew kotlin:06-data-access:bootRun
```

### Test

```bash
cd $(git rev-parse --show-toplevel) && \
    ./gradlew kotlin:06-data-access:test
```

## See Also

For the Java implementation of this module, see the [Java version](../../java/06-data-access).

[← Back to Main README](../../README.md)
