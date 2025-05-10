# 03 Bootstrap Application (Java)

[← Back to Main README](../../README.md)

This module is based on concepts from the [bootstrap repository](https://github.com/reactive-spring-book/bootstrap) with my adaptations.

This module demonstrates core Spring Boot concepts including:

- Starter dependencies
- Auto-configuration
- Transaction management
- Profile-specific configurations
- Actuator endpoints (e.g. `/actuator/beans`, `/actuator/configprops`, `/actuator/conditions`, `/actuator/threaddump`)

## Implementation Details

- Uses Spring Boot's `@Transactional` annotation and `TransactionTemplate` for transaction managment
- Configures different Actuator endpoint exposures for dev/prod environments
- Uses component scanning with `@Service` annotations
- Implements custom test infrastructure for profile-specific testing

## Running the Project

**Build:**

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :java:03-bootstrap:clean \
             :java:03-bootstrap:build
```

**Run App:**

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew :java:03-bootstrap:bootRun
```

**Run Test:**

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew java:03-bootstrap:test
```

## See Also

For the Kotlin implementation of this module, see the [Kotlin version](../../kotlin/03-bootstrap).

[← Back to Main README](../../README.md)
