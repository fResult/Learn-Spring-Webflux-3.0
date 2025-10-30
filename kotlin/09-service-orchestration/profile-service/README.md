# Profile Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

**Profile Service** provides information related to `Profile` entities attached to the `Customer`.\
This is a one-to-one relationship with the `Customer` data (managed by [Customer Service](../customer-service/README.md)).\
Each customer has one profile with a username and password.

## Implementation Details

- Provides REST endpoint `/profiles/:id` for retrieving individual profile data ([`ProfileRestController.kt`](src/main/kotlin/com/fResult/orchestration/ProfileRestController.kt))
- Returns `Mono<Profile>` for single profile lookup by ID
- Generates random password using `UUID.randomUUID()` for each profile on startup
- Stores profile data in-memory as `Map<Int, Profile>` with pre-defined usernames ([`ProfileRestController.kt`](src/main/kotlin/com/fResult/orchestration/ProfileRestController.kt))
- Pre-populates profiles for customer IDs 1 (username: "Jane") and 2 (username: "Mia")
- Profile model contains `id`, `username`, and `password` fields ([`Profile.kt`](src/main/kotlin/com/fResult/orchestration/Profile.kt))
- Registers with Eureka server using dynamic instance ID ([`application.yml`](src/main/resources/application.yml))
- Uses random port assignment (`server.port: 0`) for multiple instances
- One-to-one relationship: Each customer ID maps to exactly one profile

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
