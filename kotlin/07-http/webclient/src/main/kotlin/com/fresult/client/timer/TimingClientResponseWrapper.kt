package com.fresult.client.timer

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.client.reactive.ClientHttpResponse
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.support.ClientResponseWrapper
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

class TimingClientResponseWrapper(delegate: ClientResponse) : ClientResponseWrapper(delegate) {
  companion object {
    val log: Logger = LogManager.getLogger(TimingClientResponseWrapper::class.java)
  }

  private fun start(): Unit = log.info("Start @ {}", Instant.now())

  private fun stop(): Unit = log.info("Stop @ {}", Instant.now())

  private fun <T> log(c: Mono<T>): Mono<T> =
    c.doOnSubscribe { subscription ->
      start()
    }.doFinally { signalType ->
      stop()
    }

  private fun <T> log(c: Flux<T>): Flux<T> =
    c.doOnSubscribe { subscription ->
      start()
    }.doFinally { signalType ->
      stop()
    }

  override fun <T : Any> body(extractor: BodyExtractor<T, in ClientHttpResponse>): T =
    super.body(extractor).let { body ->
      @Suppress("UNCHECKED_CAST")
      when (body) {
        is Flux<*> -> log(body)
        is Mono<*> -> log(body)
        else -> body
      } as T
    }

  override fun <T : Any> bodyToMono(elementClass: Class<out T>): Mono<T?> =
    super.bodyToMono(elementClass).let(::log)

  override fun <T : Any> bodyToMono(elementTypeRef: ParameterizedTypeReference<T>): Mono<T> =
    super.bodyToMono(elementTypeRef).let(::log)

  override fun <T : Any> bodyToFlux(elementClass: Class<out T>): Flux<T> =
    super.bodyToFlux(elementClass)

  override fun <T : Any> bodyToFlux(elementTypeRef: ParameterizedTypeReference<T>): Flux<T> =
    super.bodyToFlux(elementTypeRef)
}
