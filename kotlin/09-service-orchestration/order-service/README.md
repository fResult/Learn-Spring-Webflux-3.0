# Order Service

[← Back to \[09 Service Orchestration\]'s README](../README.md)

**Order Service** provides all the orders that belong to `Customer`.\
This service works with [Customer Service](../customer-service/README.md) in a one-to-many relationship with each `Customer` able to have many `Order` instances.

## Implementation Details

- Provides REST endpoint `/orders` for retrieving order data ([`OrderRestController.kt`](src/main/kotlin/com/fResult/orchestration/OrderRestController.kt))
- Supports filtering by customer IDs using `ids` query parameter (e.g., `/orders?customer-ids=1,2,3`)
- Returns `Flux<Order>` stream for reactive processing
- Generates random orders for each customer (1-10 orders per customer) on startup ([`OrderRestController.kt`](src/main/kotlin/com/fResult/orchestration/OrderRestController.kt))
- Uses `UUID.randomUUID()` for unique order IDs
- Stores order data in-memory as `ConcurrentHashMap<CustomerId, List<Order>>` with thread-safe `CopyOnWriteArrayList`
- Pre-populates orders for customers with IDs 0-9 during initialization
- Filters orders by matching customer IDs from query parameter
- Registers with Eureka server using dynamic instance ID ([`application.yml`](src/main/resources/application.yml))
- Uses random port assignment (`server.port: 0`) for multiple instances
- Order model contains `id` (UUID) and `customerId` fields ([`Order.kt`](src/main/kotlin/com/fResult/orchestration/Order.kt))

## Available Scripts

### Building Application

```shell
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:order-service:clean \
            :kotlin:09-service-orchestration:order-service:build
```

### Running Application

```bash
cd $(git rev-parse --show-toplevel) && \
  ./gradlew :kotlin:09-service-orchestration:order-service:bootRun
```

When the application starts, we should see a log entry similar to the following, indicating that the service has successfully registered with the Eureka server:

```console
Registered instance ORDER-SERVICE/order-service:e975b630c01d096d7770ea0f6c1813bb with status UP (replication=true):09-service-orchestration:eureka-service:bootRun
```

[← Back to \[09 Service Orchestration\]'s README](../README.md)
