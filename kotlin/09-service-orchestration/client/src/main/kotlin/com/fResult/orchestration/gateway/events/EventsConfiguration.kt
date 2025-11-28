package com.fResult.orchestration.gateway.events

import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent
import org.springframework.cloud.gateway.route.CachingRouteLocator
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import reactor.util.Loggers

/**
 * From 12.8.5 - Events
 *
 * - [Reactive Spring Book GitHub's EventsConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/EventsConfiguration.java)
 */
@Configuration
@Profile("routes-events")
class EventsConfiguration {
  companion object {
    private val log = Loggers.getLogger(EventsConfiguration::class.java)
  }

  @EventListener
  fun refreshRoutesResultEvent(event: RefreshRoutesResultEvent) {
    log.info(event.javaClass.simpleName)
    log.info("Routes refreshed: ${event.source}")

    val source = event.source
    when (source) {
      is CachingRouteLocator -> {
        source.routes.subscribe { route -> log.info("{}:{}:{}", route.javaClass, route.metadata, route.filters) }
      }

      else -> log.warn("The source must be an instance of {}", CachingRouteLocator::class.simpleName)
    }
  }

  @Bean
  fun gateway(builder: RouteLocatorBuilder): RouteLocator =
    builder
      .routes()
      .route(::routeForSpringGuides)
      .build()

  private fun routeForSpringGuides(spec: PredicateSpec): Buildable<Route> =
    spec.path("/").filters(::guidesPathFilters).uri("https://spring.io")

  private fun guidesPathFilters(spec: GatewayFilterSpec): UriSpec = spec.setPath("/guides")
}