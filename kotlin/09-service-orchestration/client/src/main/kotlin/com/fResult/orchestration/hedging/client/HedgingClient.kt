package com.fResult.orchestration.hedging.client

import com.fResult.orchestration.GreetingResponse
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.Loggers

@Component
@Profile("hedging")
class HedgingClient(private val http: WebClient) {
  companion object {
    private val log = Loggers.getLogger(HedgingClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    http.get()
      .uri("http://slow-service/greetings")
      .retrieve()
      .bodyToFlux(GreetingResponse::class.java)
      .doOnNext(::onGreetingsReceived)
      .doOnError(::onGreetingsError)
      .subscribe()
  }

  private fun onGreetingsReceived(greeting: GreetingResponse): Unit =
    log.info("Hedging client received greeting: {}", greeting)

  private fun onGreetingsError(ex: Throwable): Unit =
    log.error("Hedging failed: {}", ex.message, ex)
}
