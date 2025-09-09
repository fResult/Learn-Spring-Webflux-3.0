package com.fresult.client

import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class AuthenticatedClient(private val webClient: WebClient) {
  fun getAuthenticatedGreeting(): Mono<Greeting> {
    return webClient.get()
      .uri("/greet/authenticated")
      .retrieve()
      .bodyToMono(Greeting::class.java)
  }
}
