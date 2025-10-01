package com.fResult.rsocket.encoding.client

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Component

@Component
class EncodingClient(private val requester: RSocketRequester) {
  companion object {
    private val log: Logger = LogManager.getLogger(EncodingClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    requester.route("greetings")
      .data(GreetingRequest("Spring fans"))
      .retrieveMono(GreetingResponse::class.java)
      .subscribe(::onGreetingReceived, ::onGreetingError, ::onGreetingComplete)
  }

  private fun onGreetingReceived(greeting: GreetingResponse) {
    log.info("Received response: {}", greeting.message)
  }

  private fun onGreetingError(ex: Throwable): Unit =
    log.error("Error receiving greeting, {}", ex.message, ex)

  private fun onGreetingComplete() = log.info("Completed receiving greeting")
}
