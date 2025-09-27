package com.fResult.rsocket.requestResponse.client

import com.fResult.rsocket.dsl.retry.RetryConfigBuilder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.nio.channels.ClosedChannelException

@Component
class RequestResponseClient(private val requester: RSocketRequester) {
  companion object {
    private val log: Logger = LogManager.getLogger(RequestResponseClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReadyEvent() {
    log.info("The data mime type is [{}]", requester.dataMimeType())
    log.info("The metadata mime type is [{}]", requester.metadataMimeType())

    requester.route("greeting")
      .data("Reactive RSocket")
      .retrieveMono(String::class.java)
      .retryWhen(retryBackoffOnClosedChannel { maxAttempts = 5 })
      .subscribe(::onGreetingSuccess, ::onGreetingFailure, ::onGreetingComplete)
  }

  private fun onGreetingSuccess(value: String): Unit = log.info("Received response: {}", value)
  private fun onGreetingFailure(ex: Throwable): Unit = log.error(ex.message, ex)
  private fun onGreetingComplete(): Unit = log.info("Request-Response interaction completed")

  private fun retryBackoffOnClosedChannel(init: RetryConfigBuilder.() -> Unit = {}): RetryBackoffSpec {
    val cfg = RetryConfigBuilder().apply(init).build()

    return Retry.backoff(cfg.maxAttempts, cfg.firstBackOff)
      .maxBackoff(cfg.maxBackoff)
      .filter { it is ClosedChannelException }
      .jitter(0.2)
      .onRetryExhaustedThrow { _, _ -> RuntimeException("Retries exhausted") }
  }
}
