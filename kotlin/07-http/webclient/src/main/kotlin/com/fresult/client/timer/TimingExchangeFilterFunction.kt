package com.fresult.client.timer

import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction


import reactor.core.publisher.Mono

class TimingExchangeFilterFunction : ExchangeFilterFunction {
  override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> =
    next.exchange(request).map { TimingClientResponseWrapper(it) }
}
