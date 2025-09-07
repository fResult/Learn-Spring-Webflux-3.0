package com.fResult.http.routes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.kotlin.core.publisher.toMono

@Configuration
class SimpleFunctionalEndpointConfiguration {
  @Bean
  fun simple(handler: GreetingsHandlerFunction): RouterFunction<ServerResponse> =
    RouterFunctions.route()
      .GET("/hello/{name}") { request ->
        request.pathVariable("name").toMono()
          .map { "Hello, $it!" }
          .flatMap(ServerResponse.ok()::bodyValue)
      }
      .GET("/hodor", handler)
      .GET("sup", handler::handle)
      .build()
}
