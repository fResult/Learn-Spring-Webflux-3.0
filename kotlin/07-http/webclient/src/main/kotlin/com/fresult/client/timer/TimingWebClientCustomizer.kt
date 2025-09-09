package com.fresult.client.timer

import org.springframework.boot.webclient.WebClientCustomizer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class TimingWebClientCustomizer : WebClientCustomizer {
  override fun customize(webClientBuilder: WebClient.Builder) {
    webClientBuilder.filter(TimingExchangeFilterFunction())
  }
}
