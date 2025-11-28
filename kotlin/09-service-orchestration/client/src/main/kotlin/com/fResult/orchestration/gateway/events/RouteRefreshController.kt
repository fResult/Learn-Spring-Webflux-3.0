package com.fResult.orchestration.gateway.events

import org.springframework.cloud.gateway.event.RefreshRoutesEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Profile("routes-events")
@RestController
class RouteRefreshController(private val eventPublisher: ApplicationEventPublisher) {
  /**
   * Endpoint to trigger a route refresh event, simurating Actuator's `/actuator/gateway/refresh`
   */
  @GetMapping("/admin/refresh-routes")
  fun refreshRoutes(): Mono<ResponseEntity<Map<String, String>>> {
    eventPublisher.publishEvent(RefreshRoutesEvent(this))
    return Mono.just(ResponseEntity.ok(mapOf("status" to "Routes refresh event published")))
  }
}
