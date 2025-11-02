package com.fResult.orchestration.gateway.rateLimiter

import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory
import org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.RestController

/**
 * From 12.8.3 - Filter
 *
 * - [Reactive Spring Book GitHub's RateLimiterConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/RateLimiterConfiguration.java)
 */
@Configuration
@RestController
@Profile("routes-limiter")
class RateLimiterConfiguration {
  @Bean
  fun redisRateLimiter(): RedisRateLimiter = RedisRateLimiter(1, 3)

  @Bean
  fun rateLimiterGateway(builder: RouteLocatorBuilder, redisRateLimiter: RedisRateLimiter): RouteLocator =
    builder.routes().route(routeForOk(redisRateLimiter)).route(routeForCustomers(redisRateLimiter)).build()

  /**
   * Rate limiting configuration for **custom endpoint** `/error` (mapped to error-service's `/ok`).
   *
   * ## Key Concepts
   * | Endpoint Type       | URL Pattern              | Rate Limited? | Use Case                          |
   * |---------------------|--------------------------|---------------|-----------------------------------|
   * | **Custom Endpoint** | `localhost:8080/error`   | ✅ Yes        | When you want rate limiting       |
   * | **Normal URL**      | `localhost:8080/error-service/ok` | ❌ No | Standard service access (bypasses gateway rate limits) |
   */
  private fun routeForOk(redisRateLimiter: RedisRateLimiter): (PredicateSpec) -> Buildable<Route> = { spec ->
    spec.path("/error")
      .filters(errorOkPathFilters(redisRateLimiter))
      .uri("lb://error-service")
  }

  /**
   * Rate limiting configuration for **custom endpoint** `/customer`.
   *
   * ## Key Concepts
   * | Endpoint Type       | URL Pattern               | Rate Limited? |
   * |---------------------|---------------------------|---------------|
   * | **Custom Endpoint** | `localhost:8080/customer` | ✅ Yes        |
   * | **Normal URL**      | `localhost:8080/customer-service/customers` | ❌ No |
   */
  private fun routeForCustomers(redisRateLimiter: RedisRateLimiter): (PredicateSpec) -> Buildable<Route> = { spec ->
    spec.path("/customer")
      .filters(customersPathFilters(redisRateLimiter))
      .uri("lb://customer-service")
  }

  private fun errorOkPathFilters(redisRateLimiter: RedisRateLimiter): (GatewayFilterSpec) -> UriSpec = { spec ->
    spec.setPath("/ok").requestRateLimiter(rateLimiterConfig(redisRateLimiter))
  }

  private fun customersPathFilters(redisRateLimiter: RedisRateLimiter): (GatewayFilterSpec) -> UriSpec = { spec ->
    spec.setPath("/customers").requestRateLimiter(rateLimiterConfig(redisRateLimiter))
  }

  private fun rateLimiterConfig(redisRateLimiter: RedisRateLimiter): (RequestRateLimiterGatewayFilterFactory.Config) -> Unit =
    { config ->
      config.setRateLimiter(redisRateLimiter)
        .setKeyResolver(PrincipalNameKeyResolver())
    }
}
