package com.fResult.http.routes

import com.fResult.http.routes.CaseInsensitiveRequestPredicates.Companion.i
import com.fResult.utils.respondWithOkStreamBody
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.kotlin.core.publisher.toMono

@Configuration
class CustomRoutePredicatesConfiguration {
  companion object {
    private val HAD_PERMISSION_UID = setOf("1", "2", "3")
  }

  val handler: HandlerFunction<ServerResponse> = HandlerFunction { request ->
    request.queryParam("name")
      .toMono()
      .map { "Hello, ${it.orElseGet { request.pathVariable("name") }}!" }
      .let(::respondWithOkStreamBody)
  }

  @Bean
  fun customRequestPredicates(): RouterFunction<ServerResponse> {
    val peculiarRequestPredicate = RequestPredicates.GET("/test")
      .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
      .and(::isRequestForValidUid)

    val insensitiveRequestPredicate = i(RequestPredicates.GET("/greetings/{name}"))

    return RouterFunctions.route()
      .add(RouterFunctions.route(peculiarRequestPredicate, handler))
      .add(RouterFunctions.route(insensitiveRequestPredicate, handler))
      .build()
  }

  private fun isRequestForValidUid(request: ServerRequest): Boolean =
    request.queryParam("uid")
      .map(HAD_PERMISSION_UID::contains)
      .orElse(false)
}
