package com.fResult.http.routes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import java.nio.charset.Charset

@Configuration
class NestedFunctionalEndpointConfiguration {
  @Bean
  fun nested(nestedHandler: NestedHandler): RouterFunction<ServerResponse> {
    val jsonRP = RequestPredicates.accept(MediaType.APPLICATION_JSON)
      // NOTE: Actually, this is not necessary as the default charset for JSON is UTF-8, since the major browsers support UTF-8 by default.
      .or(RequestPredicates.accept(MediaType(MediaType.APPLICATION_JSON, Charset.forName("UTF-8"))))

    val sseRP = RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM)

    return RouterFunctions.route()
      .nest(RequestPredicates.path("/nested")) { builder ->
        builder.nest(jsonRP) { nestedBuilder ->
          nestedBuilder.GET("/{pv}", nestedHandler::pathVariable)
          nestedBuilder.GET("", nestedHandler::noPathVariable)
        }.add(RouterFunctions.route(sseRP, nestedHandler::sse))
      }.build()
  }
}
