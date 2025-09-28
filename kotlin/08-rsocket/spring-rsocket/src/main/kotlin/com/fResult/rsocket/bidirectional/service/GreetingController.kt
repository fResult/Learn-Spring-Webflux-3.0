package com.fResult.rsocket.bidirectional.service

import com.fResult.rsocket.bidirectional.ClientHealthState
import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Controller
class GreetingController {
  companion object {
    val log: Logger = LogManager.getLogger(GreetingController::class.java)
  }

  @MessageMapping("greetings")
  fun greetings(requester: RSocketRequester, @Payload body: GreetingRequest): Flux<GreetingResponse> {
    val onClientStopped = requester.route("health")
      .data(Mono.empty<Any>())
      .retrieveFlux(ClientHealthState::class.java)
      .filter(::isClientHealthStateStopped)
      .doOnNext { log.info(it) }

    return Flux.fromStream(Stream.generate(greetingResponderFrom(body)))
      .delayElements(randomDelayUpTo10Seconds())
      .takeUntilOther(onClientStopped)
      .doOnNext { log.info("Sending greeting: {}", it) }
      .doOnError { ex -> log.error("Greeting error: {}", ex.message, ex) }
      .doFinally { log.info("Finished greeting to ${body.name}") }
  }

  private fun isClientHealthStateStopped(chs: ClientHealthState) =
    ClientHealthState.STOPPED.equals(chs.state, ignoreCase = true)

  private fun greetingResponderFrom(request: GreetingRequest): () -> GreetingResponse = {
    GreetingResponse("Hello, ${request.name} @ ${Instant.now()}")
  }

  private fun randomDelayUpTo10Seconds() = (3..10).random().seconds.toJavaDuration()
}
