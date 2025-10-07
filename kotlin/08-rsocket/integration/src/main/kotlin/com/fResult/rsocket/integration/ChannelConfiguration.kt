package com.fResult.rsocket.integration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.MessageChannels
import org.springframework.messaging.MessageChannel

@Configuration
class ChannelConfiguration {
  @Bean
  fun messageChannel(): MessageChannel = MessageChannels.flux().`object`
}
