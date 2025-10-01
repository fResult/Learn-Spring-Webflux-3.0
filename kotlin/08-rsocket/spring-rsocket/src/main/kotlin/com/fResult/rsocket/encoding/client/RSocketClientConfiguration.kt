package com.fResult.rsocket.encoding.client

import com.fResult.rsocket.FResultProperties
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester

@Configuration
class RSocketClientConfiguration {
  @Bean
  fun rSocketRequester(
    properties: FResultProperties,
    builder: RSocketRequester.Builder,
  ): RSocketRequester =
    builder.tcp(properties.rsocket.hostname, properties.rsocket.port)

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  fun rSocketStrategiesCustomizer() = RSocketStrategiesCustomizer { strategies ->
    strategies
      .decoder(Jackson2JsonDecoder())
      .encoder(Jackson2JsonEncoder())
  }
}
