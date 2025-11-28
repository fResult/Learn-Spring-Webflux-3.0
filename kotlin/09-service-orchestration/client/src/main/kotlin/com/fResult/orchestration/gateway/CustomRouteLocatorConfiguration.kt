package com.fResult.orchestration.gateway

import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.SetPathGatewayFilterFactory
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * From 12.8.6 - Alternative Configuration
 *
 * - [Reactive Spring Book GitHub's CustomRouteLocatorConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/CustomRouteLocatorConfiguration.java)
 */
@Configuration
@Profile("routes-custom")
class CustomRouteLocatorConfiguration {
  @Bean
  fun customRouteLocator(setPathGatewayFilterFactory: SetPathGatewayFilterFactory): RouteLocator {
    val setPathGatewayFilter =
      setPathGatewayFilterFactory.apply { config -> config.template = "/guides/" }
    val orderedGatewayFilter = OrderedGatewayFilter(setPathGatewayFilter, 0)
    val singleRoute = Route.async()
      .id("spring-io-guides")
      .asyncPredicate(::anyPathPredicate)
      .filter(orderedGatewayFilter)
      .uri("https://spring.io")
      .build()

    return RouteLocator { Flux.just(singleRoute) }
  }

  fun anyPathPredicate(ignored: ServerWebExchange): Mono<Boolean> = Mono.just(true)
}
