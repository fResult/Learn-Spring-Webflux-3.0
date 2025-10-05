package com.fResult.rsocket.client

import com.fResult.rsocket.dsl.retry.retryBackoffOnClosedChannel
import com.fResult.rsocket.dtos.GreetingResponse
import io.rsocket.metadata.WellKnownMimeType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata
import org.springframework.stereotype.Component
import org.springframework.util.MimeTypeUtils
import reactor.core.publisher.Mono

@Component
class SecurityClient(
  private val requester: RSocketRequester,
  private val credentials1: UsernamePasswordMetadata,
  private val credentials2: UsernamePasswordMetadata,
) {

  companion object {
    private val log: Logger = LogManager.getLogger(SecurityClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReadyEvent() {
    log.info("Streaming greetings secured with different users...");
    credentials1.let(::fetchGreetingStream).subscribe(::onGreetReceived)
    credentials2.let(::fetchGreetingStream).subscribe(::onGreetReceived)
  }

  private fun fetchGreetingStream(credentials: UsernamePasswordMetadata) =
    requester.route("greetings")
      .metadata(credentials, MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.string))
      .data(Mono.empty<Void>())
      .retrieveFlux(GreetingResponse::class.java)
      .retryWhen(retryBackoffOnClosedChannel { maxAttempts = 5 })

  private fun onGreetReceived(greeting: GreetingResponse): Unit =
    log.info("Secured Response Received: {}", greeting)
}
