package com.fResult.rsocket.service

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.util.stream.Stream
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@Controller
class GreetingController {
  companion object {
    private val log: Logger = LogManager.getLogger(GreetingController::class.java)
  }

  @OptIn(ExperimentalTime::class)
  @MessageMapping("greetings")
  fun greet(body: GreetingRequest): Flux<GreetingResponse> =
    Flux.fromStream { Stream.generate { GreetingResponse("Hello, ${body.name} @ ${Clock.System.now()}") } }
      .take(10)
      .delayElements(1.seconds.toJavaDuration())
      .doOnNext(::onGreetSent)

  fun onGreetSent(greet: GreetingResponse): Unit = log.info("onGreetSend: $greet")
}
