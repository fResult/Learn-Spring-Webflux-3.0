package com.fResult.orchestration.gateway

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("routes-simple")
class SimpleProxyRouteConfiguration {
  @Bean
  fun gateway(builder: RouteLocatorBuilder): RouteLocator =
    builder.routes().route(::springIoRoute).build()

  fun springIoRoute(spec: PredicateSpec): Buildable<Route> = spec.alwaysTrue().uri("https://spring.io")
}
