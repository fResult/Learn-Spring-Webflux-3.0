package com.fResult.orchestration.hedging.config

import com.fResult.orchestration.hedging.HedgingFilterFunction
import com.fResult.orchestration.hedging.qualifier.HedgingWebClient
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class HedgingConfiguration(
  private val discoveryClient: ReactiveDiscoveryClient,
  private val hedgingLbProps: HedgingLoadBalancerProperties,
) {
  @Bean
  @HedgingWebClient
  fun hedgingWebClient(builder: WebClient.Builder, filter: HedgingFilterFunction): WebClient =
    builder.filter(filter).build()

  @Bean
  fun hedgingFilterFunction(): HedgingFilterFunction = HedgingFilterFunction(
    discoveryClient,
    hedgingLbProps.maxNodes,
  )
}
