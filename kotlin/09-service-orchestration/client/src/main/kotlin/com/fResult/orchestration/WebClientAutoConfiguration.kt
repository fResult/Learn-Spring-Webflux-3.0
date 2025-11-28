package com.fResult.orchestration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.Logger
import reactor.util.Loggers

@Configuration
class WebClientAutoConfiguration {
  companion object {
    private val log: Logger = Loggers.getLogger(WebClientAutoConfiguration::class.java)
  }

  @Bean
  @ConditionalOnMissingBean
  fun loadBalancingWebClient(
    builder: WebClient.Builder,
    lbFunction: ReactorLoadBalancerExchangeFilterFunction,
  ): WebClient {
    log.info("Registering a default load-balanced [{}] bean.", WebClient::class.java)

    return builder.filter(lbFunction).build()
  }
}
