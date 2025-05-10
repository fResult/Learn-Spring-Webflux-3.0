# 03 Bootstrap Application (Kotlin)

[← Back to Main README](../../README.md)

This module is based on concepts from the [bootstrap repository](https://github.com/reactive-spring-book/bootstrap) with my adaptations.

This module demonstrates core Spring Boot concepts in Kotlin including:
- Starter dependencies 
- Auto-configuration
- Transaction management
- Profile-specific configurations
- Actuator endpoints (e.g. `/actuator/beans`, `/actuator/configprops`, `/actuator/conditions`, `/actuator/threaddump`)

## Implementation Details

- Leverages Kotlin language features like extension functions and nullable types
- Implements transaction management using Spring Boot's Kotlin support
- Configures Actuator endpoints for different profiles

## Running the Project

**Run App:**

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew kotlin:03-bootstrap:bootRun
```

**Run Test:**

```bash
cd $(git rev-parse --show-toplevel) && \
   ./gradlew kotlin:03-bootstrap:test
```

## See Also

For the Java implementation of this module, see the [Java version](../../java/03-bootstrap).

[← Back to Main README](../../README.md)
