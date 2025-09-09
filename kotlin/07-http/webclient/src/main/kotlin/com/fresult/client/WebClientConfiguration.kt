package com.fresult.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {
  @Bean
  fun webClient(builder: WebClient.Builder): WebClient =
    ExchangeFilterFunctions.basicAuthentication("MyUser", "MyPassword")
      .let { authFilter ->
        builder.filter(authFilter).build()
      }
}
