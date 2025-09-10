package com.fresult.servlets

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
class GreetingsRouteConfiguration {
  @Bean
  fun routes(): RouterFunction<ServerResponse> = coRouter {
    GET("/hello/functional/{name}", ::helloFunctional)
  }
}

private suspend fun helloFunctional(request: ServerRequest): ServerResponse {
  val greetWithName: (String) -> Mono<Greetings> = { Greetings.greet("functional", it) }

  return request.pathVariable("name")
    .toMono()
    .flatMap(greetWithName)
    .flatMap(ServerResponse.ok()::bodyValue)
    .awaitSingle()
}
