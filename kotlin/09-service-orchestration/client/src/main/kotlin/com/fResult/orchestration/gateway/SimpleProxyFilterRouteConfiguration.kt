package com.fResult.orchestration.gateway

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("routes-filter-simple")
class SimpleProxyFilterRouteConfiguration {
  companion object {
    const val TARGET_URI = "https://httpbin.org"
  }

  @Bean
  fun filterGateway(builder: RouteLocatorBuilder): RouteLocator =
    builder.routes()
      .route(::routeForOrderToFormPost)
      .route(::routeForStatusCodeRewrite)
      .build()

  fun routeForOrderToFormPost(spec: PredicateSpec): Buildable<Route> =
    spec.path("/order").filters(::transformToFormPostPath).uri(TARGET_URI)

  fun routeForStatusCodeRewrite(spec: PredicateSpec): Buildable<Route> =
    spec.path("/code/**").filters(::rewriteStatusCodeToStatusEndpoint).uri(TARGET_URI)

  fun transformToFormPostPath(spec: GatewayFilterSpec): UriSpec =
    spec.setPath("/forms/post")

  fun rewriteStatusCodeToStatusEndpoint(spec: GatewayFilterSpec): UriSpec =
    spec.rewritePath("/code/(?<code>\\d+)", $$"/status/${code}")
}
