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

/**
 * From 12.8.3 - Filter
 *
 * - [Reactive Spring Book GitHub's RateLimiterConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/RateLimiterConfiguration.java)
 */
@Configuration
@Profile("routes-limiter")
class RateLimiterConfiguration {
  @Bean
  fun redisRateLimiter(): RedisRateLimiter = RedisRateLimiter(5, 7)

  @Bean
  fun rateLimiterGateway(builder: RouteLocatorBuilder, redisRateLimiter: RedisRateLimiter): RouteLocator =
    builder.routes().route(routeForOk(redisRateLimiter)).build()

  private fun routeForOk(redisRateLimiter: RedisRateLimiter): (PredicateSpec) -> Buildable<Route> = { spec ->
    spec.path("/ok")
      .filters(okPathFilters(redisRateLimiter))
      .uri("lb://error-service")
  }

  private fun okPathFilters(redisRateLimiter: RedisRateLimiter): (GatewayFilterSpec) -> UriSpec = { spec ->
    spec.setPath("/ok").requestRateLimiter(rateLimiterConfig(redisRateLimiter))
  }

  private fun rateLimiterConfig(redisRateLimiter: RedisRateLimiter): (RequestRateLimiterGatewayFilterFactory.Config) -> Unit =
    { config ->
      config.setRateLimiter(redisRateLimiter)
        .setKeyResolver(PrincipalNameKeyResolver())
    }
}