package com.fResult.rsocket.service

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Stream
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@Controller
class GreetingController {
  @MessageMapping("greetings")
  fun greet(@AuthenticationPrincipal user: Mono<UserDetails>): Flux<GreetingResponse> =
    user.map(UserDetails::getUsername)
      .map { GreetingRequest(it) }
      .flatMapMany(::greet)

  @OptIn(ExperimentalTime::class)
  fun greet(request: GreetingRequest): Flux<GreetingResponse> =
    Flux.fromStream(Stream.generate { GreetingResponse("Hello, ${request.name} @ ${Clock.System.now()}!") })
      .delayElements(1.seconds.toJavaDuration())
}
