package com.fresult.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {
  @Bean
  fun webClient(): WebClient =
    ExchangeFilterFunctions.basicAuthentication("fResult", "password1")
      .let { basicAuthFilter ->
        WebClient.builder()
          .filter(basicAuthFilter)
          .build()
      }
}
