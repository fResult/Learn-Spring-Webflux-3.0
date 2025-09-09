package com.fresult.client

import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DefaultClient(private val client: WebClient) {
  fun getSingle(name: String): Mono<Greeting> = client.get()
    .uri(
      "/greet/single/{name}",
      mapOf("name" to name),
    )
    .retrieve()
    .bodyToMono(Greeting::class.java)

  fun getMany(name: String): Flux<Greeting> = client.get()
    .uri(
      "/greet/many/{name}",
      mapOf("name" to name),
    )
    .retrieve()
    .bodyToFlux(Greeting::class.java)
    .take(10)
}
