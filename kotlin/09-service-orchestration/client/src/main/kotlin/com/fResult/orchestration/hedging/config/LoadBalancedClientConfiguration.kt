package com.fResult.orchestration.hedging.config

import com.fResult.orchestration.hedging.qualifier.LoadBalancedWebClient
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class LoadBalancedClientConfiguration {
  @Bean
  @LoadBalancedWebClient
  fun loadBalancedWebClient(builder: WebClient.Builder, lbFunction: ReactorLoadBalancerExchangeFilterFunction): WebClient =
    builder.filter(lbFunction).build()
}
