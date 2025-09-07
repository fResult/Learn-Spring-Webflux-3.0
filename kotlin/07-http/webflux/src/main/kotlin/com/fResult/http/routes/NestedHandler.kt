package com.fResult.http.routes

import com.fResult.utils.IntervalMessageProducer
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class NestedHandler {
  fun pathVariable(request: ServerRequest): Mono<ServerResponse> =
    request.pathVariable("pv").toMono()
      .map(::greet)
      .flatMap { ServerResponse.ok().bodyValue(it) }

  fun noPathVariable(ignored: ServerRequest): Mono<ServerResponse> =
    greet().let { ServerResponse.ok().bodyValue(it) }

  fun sse(request: ServerRequest): Mono<ServerResponse> =
    ServerResponse.ok()
      .contentType(MediaType.TEXT_EVENT_STREAM)
      .body(IntervalMessageProducer.produce(), String::class.java)

  private fun greet(name: String? = null): Map<String, String> =
    mapOf("message" to "Hello, ${name ?: "World"}!")
}
