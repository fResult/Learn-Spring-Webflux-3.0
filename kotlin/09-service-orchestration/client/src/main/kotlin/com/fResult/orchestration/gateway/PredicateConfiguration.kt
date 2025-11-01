package com.fResult.orchestration.gateway

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import reactor.core.publisher.Mono

@Configuration
@Profile("routes-predicate")
class PredicateConfiguration {
  companion object {
    const val TARGET_URL = "https://httpbin.org"
  }

  @Bean
  fun predicatesGateway(builder: RouteLocatorBuilder): RouteLocator =
    builder.routes()
      .route(::routeForRoot)
      .route(::routeForXFResultHeader)
      .route(::routeForUidQueryParam)
      .route(::routeForPostFormWithRandomPredicate)
      .build()

  private fun routeForRoot(spec: PredicateSpec): Buildable<Route> =
    spec.path("/").uri(TARGET_URL)

  private fun routeForXFResultHeader(spec: PredicateSpec): Buildable<Route> =
    spec.header("X-FRESULT").uri(TARGET_URL)

  private fun routeForUidQueryParam(spec: PredicateSpec): Buildable<Route> =
    spec.query("uid").uri(TARGET_URL)

  private fun routeForPostFormWithRandomPredicate(spec: PredicateSpec): Buildable<Route> =
    spec.asyncPredicate { _ -> Mono.just((0..9).random() >= 5) }
      .and().path("/forms/post")
      .uri(TARGET_URL)
}
