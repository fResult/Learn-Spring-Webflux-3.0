package com.fResult.rsocket.bidirectional.client

import com.fResult.rsocket.FResultProperties
import com.fResult.rsocket.bidirectional.ClientHealthState
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.stream.Stream
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Controller
class HealthController(private val props: FResultProperties) {
  companion object {
    val log: Logger = LogManager.getLogger(HealthController::class.java)
  }

  @MessageMapping("health")
  fun health(): Flux<ClientHealthState> {
    val start = Instant.now()
    val delaySeconds = (0..30).random().seconds

    return Flux.fromStream(Stream.generate(nextClientHealthState(start, delaySeconds)))
      .delayElements(5.seconds.toJavaDuration())
      .doOnNext {
        if (ClientHealthState.STARTED.equals(it.state, ignoreCase = true))
          log.info("Health state on [{}:{}] is [{}]", props.rsocket.hostname, props.rsocket.port, it.state)
        else
          log.warn("Health state on [{}:{}] is [{}]", props.rsocket.hostname, props.rsocket.port, it.state)
      }
  }

  fun nextClientHealthState(start: Instant, delay: Duration): () -> ClientHealthState = {
    val now = Instant.now()
    val stopTime = start.plus(delay.toJavaDuration())
    val shouldStop = now.isAfter(stopTime) && Math.random() > .8

    ClientHealthState(if (shouldStop) "STOPPED" else "STARTED")
  }
}
