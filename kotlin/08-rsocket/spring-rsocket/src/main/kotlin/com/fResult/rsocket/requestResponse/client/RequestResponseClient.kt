package com.fResult.rsocket.requestResponse.client

import com.fResult.rsocket.dsl.retry.retryBackoffOnClosedChannel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component

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
}
