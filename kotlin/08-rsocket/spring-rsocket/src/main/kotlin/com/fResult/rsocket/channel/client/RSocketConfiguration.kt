package com.fResult.rsocket.channel.client

import com.fResult.rsocket.FResultProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester

@Configuration
class RSocketConfiguration {
  @Bean
  fun rSocketRequester(properties: FResultProperties, builder: RSocketRequester.Builder): RSocketRequester {
    return builder.tcp(properties.rsocket.hostname, properties.rsocket.port)
  }
}
