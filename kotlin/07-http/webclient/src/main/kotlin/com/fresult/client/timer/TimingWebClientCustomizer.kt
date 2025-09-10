package com.fresult.client.timer

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient

@Component
class TimingWebClientCustomizer : WebClientCustomizer {
  override fun customize(webClientBuilder: WebClient.Builder) {
    // webClientBuilder.filter { request, next ->
    //   next.exchange(request).map { TimingClientResponseWrapper(it) }
    // }
    webClientBuilder.filter(TimingExchangeFilterFunction())
  }
}
