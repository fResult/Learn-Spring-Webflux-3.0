package com.fResult.rsocket.integration

import com.fResult.rsocket.FResultProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.rsocket.ClientRSocketConnector
import org.springframework.messaging.rsocket.RSocketStrategies

@Configuration
class RSocketConnectionConfiguration {
  @Bean
  fun clientRSocketConnector(props: FResultProperties, strategies: RSocketStrategies) =
    ClientRSocketConnector(props.rsocket.hostname, props.rsocket.port)
      .apply { rSocketStrategies = strategies }
}
