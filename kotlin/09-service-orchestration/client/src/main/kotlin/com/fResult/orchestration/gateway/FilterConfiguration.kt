package com.fResult.orchestration.gateway

import org.reactivestreams.Subscription
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.publisher.Signal
import reactor.util.Loggers
import java.net.URI
import java.util.*

/**
 * From 12.8.3 - Filter
 *
 * - [Reactive Spring Book GitHub's FilterConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/FilterConfiguration.java)
 */
@Configuration
@Profile("routes-filter")
class FilterConfiguration {
  companion object {
    private val log = Loggers.getLogger(FilterConfiguration::class.java)
  }

  @Bean
  fun filterGateway(builder: RouteLocatorBuilder): RouteLocator =
    builder.routes()
      .route(::routeToFormPost)
      .build()

  private fun routeToFormPost(spec: PredicateSpec): Buildable<Route> =
    spec.path("/")
      .filters(::formPostFilters)
      .uri("https://httpbin.org")

  private fun formPostFilters(filterSpec: GatewayFilterSpec): UriSpec =
    filterSpec.setPath("/forms/post")
      .retry(10)
      .addRequestParameter("uid", UUID.randomUUID().toString())
      .addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
      .filter(::logRequestLifecycle)

  private fun logRequestLifecycle(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
    val uri = exchange.request.uri

    return chain.filter(exchange)
      .doOnSubscribe(logRequestInitiation(uri))
      .doOnEach(logRequestProcessing(uri))
      .doOnTerminate(logRequestCompletion(uri, exchange))
  }

  private fun logRequestInitiation(uri: URI): (Subscription) -> Unit = { _ ->
    log.info("Before: $uri")
  }

  private fun logRequestProcessing(uri: URI): (Signal<*>) -> Unit = { _ ->
    log.info("Processing: $uri")
  }

  private fun logRequestCompletion(uri: URI, exchange: ServerWebExchange): () -> Unit = {
    log.info("After: $uri. → Status: ${exchange.response.statusCode}")
  }
}
