package com.fResult.orchestration.hedging.client

import com.fResult.orchestration.GreetingResponse
import com.fResult.orchestration.Order
import com.fResult.orchestration.hedging.qualifier.HedgingWebClient
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.Loggers

@Component
@Profile("hedging")
class HedgingRequestClient(
  @HedgingWebClient private val hedgingHttp: WebClient,
  private val orderClient: OrderClient,
) {
  companion object {
    private val log = Loggers.getLogger(HedgingRequestClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    hedgingHttp.get()
      .uri("http://slow-service/greetings")
      .retrieve()
      .bodyToFlux(GreetingResponse::class.java)
      .doOnNext(::onGreetingsReceived)
      .doOnError(onResponseError("Hedging"))
      .subscribe()

    orderClient.getOrders(1, 2, 3)
      .doOnNext(::onOrdersReceived)
      .doOnError(onResponseError("Load Balanced"))
      .subscribe()
  }

  private fun onGreetingsReceived(greeting: GreetingResponse): Unit =
    log.info("Hedging client received greeting: {}", greeting)

  private fun onOrdersReceived(order: Order): Unit =
    log.info("Order Client Balanced client received order: {}", order)

  private fun onResponseError(type: String): (Throwable) -> Unit = { ex ->
    log.error("$type failed: {}", ex.message, ex)
  }
}
