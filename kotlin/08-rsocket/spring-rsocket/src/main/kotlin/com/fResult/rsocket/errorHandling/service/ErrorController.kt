package com.fResult.rsocket.errorHandling.service

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Controller
class ErrorController {
  companion object {
    val log: Logger = LogManager.getLogger(ErrorController::class.java)
  }

  @MessageMapping("greetings")
  fun greet(name: String): Flux<String> = Flux.fromStream(Stream.generate { "Hello, $name!" })
    .flatMap(::greetWithRandomFailure)
    .delayElements(1.seconds.toJavaDuration())

  @MessageExceptionHandler
  fun handleException(ex: Exception): Unit = log.error("Error occurred: {}", ex.message, ex)

  private fun greetWithRandomFailure(greeting: String): Mono<String> {
    return if (Math.random() >= 0.5) Mono.error(IllegalArgumentException("Oops!"))
    else Mono.just(greeting)
  }
}
