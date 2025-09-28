package com.fResult.rsocket.fireAndForget.client

import com.fResult.rsocket.dsl.retry.retryBackoffOnClosedChannel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component

@Component
class FireAndForgetClient(private val requester: RSocketRequester) {
  companion object {
    private val log: Logger = LogManager.getLogger(FireAndForgetClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting Fire-and-Forget requests on {}", FireAndForgetClient::class.simpleName)

    (1..10).forEach { n ->
      val message = "Reactive Spring #$n"
      requester.route("greeting")
        .data(message)
        .send()
        .retryWhen(retryBackoffOnClosedChannel { maxAttempts = 5 })
        .doOnEach(logSentGreeting(message))
        .doOnError(logFailure(n))
        .subscribe()
    }
  }

  private fun <T> logSentGreeting(message: String): (T) -> Unit =
    { log.info("Sent greeting command: {}", message) }

  private fun logFailure(n: Int): (Throwable) -> Unit =
    { ex -> log.error("Failed to send greeting #{}: {}", n, ex.message, ex) }
}
