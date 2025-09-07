package com.fResult.utils

import org.reactivestreams.Publisher
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

inline fun <reified T> respondWithOkStreamBody(responseBody: Publisher<T>): Mono<ServerResponse> =
  ServerResponse.ok().body(responseBody, T::class.java)

inline fun <reified T : Any> respondWithOkResponse(responseBody: T): Mono<ServerResponse> =
  ServerResponse.ok().bodyValue(responseBody)

fun respondNotFound(): Mono<ServerResponse> = ServerResponse.notFound().build()
