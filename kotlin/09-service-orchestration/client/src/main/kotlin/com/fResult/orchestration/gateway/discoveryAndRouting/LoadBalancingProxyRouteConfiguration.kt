package com.fResult.orchestration.gateway.discoveryAndRouting

import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.Buildable
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * From 12.8.4 - Discovery and Routing
 * - [Reactive Spring Book GitHub's LoadbalancingProxyRouteConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/LoadbalancingProxyRouteConfiguration.java)
 *
 * # Manual route configuration for Spring Cloud Gateway using client-side load balancing.
 *
 * ## Key Concepts
 * - **Replaces auto-discovery** (`spring.cloud.gateway.server.webflux.discovery.locator.enabled=true`)
 * - Uses explicit `lb://` URI scheme for client-side load balancing
 * - Need to **active** this **profile `routes-loadbalanced`** to enable this configuration
 *
 * ## Why Manual Configuration?
 * When you need:
 * - Full control over routing logic (e.g., custom predicates/filters)
 * - Single-route focus (e.g., all traffic → `error-service`)
 * - Avoid auto-generated routes for specific services
 *
 * ## Comparison with Auto-Discovery
 * | Feature                | Manual (`@Profile`)               | Auto-Discovery (`discovery.locator`) |
 * |------------------------|-----------------------------------|--------------------------------------|
 * | Route Definition       | Explicit in code                  | Auto-generated from service registry |
 * | URL Pattern            | `http://gateway/any-path`         | `http://gateway/SERVICE-ID/path`     |
 * | Load Balancing         | ✅ (`lb://` scheme)               | ✅ (Built-in)                        |
 * | Custom Filters         | ✅ (Full control)                 | ⚠️ Limited                          |
 * | Service ID Case        | Preserves original case           | Requires `lower-case-service-id: true` |
 */
@Configuration
@Profile("routes-loadbalanced")
class LoadBalancingProxyRouteConfiguration {
  @Bean
  fun loadBalancedGateway(builder: RouteLocatorBuilder): RouteLocator =
    builder.routes().route(::routeForLoadBalancedErrorService).build()

  fun routeForLoadBalancedErrorService(spec: PredicateSpec): Buildable<Route> =
    spec.alwaysTrue().uri("lb://error-service")
}
