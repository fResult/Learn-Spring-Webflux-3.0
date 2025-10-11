package com.fResult.rsocket.errorHandling.service

import com.fResult.rsocket.errorHandling.GreetingException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Controller
class ErrorHandlingController {
  companion object {
    val log: Logger = LogManager.getLogger(ErrorHandlingController::class.java)
  }

  @MessageMapping("greetings")
  fun greet(name: String): Flux<String> {
    log.info("Received greeting request for: {}", name)
    return Flux.fromStream(Stream.generate { "Hello, $name!" })
      .delayElements(1.seconds.toJavaDuration())
      .flatMap(::greetWithRandomFailure)
      .onErrorContinue { ex, obj ->
        when (ex) {
          is GreetingException -> log.error("GreetingException for $obj: ${ex.message}")
          is IllegalArgumentException -> log.warn("IllegalArgumentException for $obj: ${ex.message}")
          else -> log.error("Unexpected error for $obj: ${ex.message}", ex)
        }
      }
  }

//  @MessageExceptionHandler(GreetingException::class)
//  fun handleGreetingException(ex: GreetingException): String {
//    log.error("Error occurred when greeting: {}", ex.message, ex)
//    return "Greeting error: ${ex.message}"
//  }

//  @MessageExceptionHandler(IllegalArgumentException::class)
//  fun handleIllegalArgumentException(ex: IllegalArgumentException): String {
//    log.error("Illegal argument: {}", ex.message, ex)
//    return "Illegal argument: ${ex.message}"
//  }

//  @MessageExceptionHandler(Exception::class)
//  fun handleException(ex: Exception): String {
//    log.error("General error: {}", ex.message, ex)
//    return "Error: ${ex.message}"
//  }


  private fun greetWithRandomFailure(greeting: String): Mono<String> {
    return Math.random().let {
      log.info("Random value: {}", it)
      if (it >= 0.75) Mono.error(GreetingException("Failed to greet [$greeting]!"))
      else if (it >= 0.5) Mono.error(IllegalArgumentException("Oops!"))
      else Mono.just(greeting)
    }
  }
}
