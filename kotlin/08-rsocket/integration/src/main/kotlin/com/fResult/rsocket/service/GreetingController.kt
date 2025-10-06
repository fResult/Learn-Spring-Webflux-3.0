package com.fResult.rsocket.service

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
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
  @OptIn(ExperimentalTime::class)
  @MessageMapping("greetings")
  fun greet(body: GreetingRequest): Flux<GreetingResponse> =
    Flux.fromStream { Stream.generate { GreetingResponse("Hello, ${body.name} @ ${Clock.System.now()}") } }
      .take(10)
      .delayElements(1.seconds.toJavaDuration())
}
