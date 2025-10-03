package com.fResult.rsocket.errorHandling.client

import com.fResult.rsocket.dsl.retry.retryBackoffOnClosedChannel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component

@Component
class ErrorHandlingClient(private val requester: RSocketRequester) {
  companion object {
    private val log: Logger = LogManager.getLogger(ErrorHandlingClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Requesting greetings...")

    requester.route("greetings")
      .data("Spring Fans")
      .retrieveFlux(String::class.java)
      .retryWhen(retryBackoffOnClosedChannel { maxAttempts = 5 })
      .doOnError { ex -> log.warn("Oops!", ex) }
      .subscribe(::onGreetReceived) { ex -> log.error("Error in greeting stream: {}", ex.message, ex) }
  }

  private fun onGreetReceived(greeting: String) {
    log.info("Received: {}", greeting)
  }
}
