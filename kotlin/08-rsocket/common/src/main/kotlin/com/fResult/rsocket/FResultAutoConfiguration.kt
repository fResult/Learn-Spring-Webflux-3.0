package com.fResult.rsocket

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(FResultProperties::class)
class FResultAutoConfiguration {
  @Bean
  fun encodingUtils(objectMapper: ObjectMapper) = EncodingUtils(objectMapper)

  @Bean
  fun objectMapper() = ObjectMapper()
}
