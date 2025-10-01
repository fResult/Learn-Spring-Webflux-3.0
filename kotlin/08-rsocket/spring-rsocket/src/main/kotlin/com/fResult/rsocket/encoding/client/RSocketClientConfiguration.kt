package com.fResult.rsocket.encoding.client

import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder

@Configuration
class RSocketClientConfiguration {
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  fun rSocketStrategiesCustomizer() = RSocketStrategiesCustomizer { strategies ->
    strategies
      .decoder(Jackson2JsonDecoder())
      .encoder(Jackson2JsonEncoder())
  }
}
