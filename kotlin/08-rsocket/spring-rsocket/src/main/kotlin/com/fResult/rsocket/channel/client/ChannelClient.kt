package com.fResult.rsocket.channel.client

import com.fResult.rsocket.dsl.retry.retryBackoffOnClosedChannel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class ChannelClient(private val requester: RSocketRequester) {
  companion object {
    val log: Logger = LogManager.getLogger(ChannelClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("The data mime type is [{}]", requester.dataMimeType())
    log.info("The metadata mime type is [{}]", requester.metadataMimeType())

    Flux.interval(1.seconds.toJavaDuration())
      .map { i -> "Ping #${i + 1}" }
      .let(createPongRequest(requester))
      .subscribe(::onPongSuccess, ::onPongFailure, ::onPongComplete)
  }

  private fun createPongRequest(requester: RSocketRequester): (Flux<String>) -> Flux<String> =
    { pingFlux: Flux<String> ->
      requester.route("pong")
        .data(pingFlux.doOnNext(::logSendingRequest))
        .retrieveFlux(String::class.java)
        .retryWhen(retryBackoffOnClosedChannel { maxAttempts = 5 })
    }

  private fun logSendingRequest(ping: String): Unit = log.info("Sending request: {}", ping)
  private fun onPongSuccess(pong: String): Unit = log.info("Received response: {}", pong)
  private fun onPongFailure(ex: Throwable): Unit = log.error("Client stream failed with error: {}", ex.message, ex)
  private fun onPongComplete(): Unit = log.info("Client stream completed")
}
