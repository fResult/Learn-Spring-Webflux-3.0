package com.fResult.orchestration.reactor

import com.fResult.orchestration.Order
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.util.Loggers

@Component
class DegradingClient(private val orderClient: OrderClient) {
  companion object {
    private val log = Loggers.getLogger(DegradingClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    orderClient.getOrders(1, 2)
      .onErrorResume { _ -> Flux.empty() }
      .subscribe(::onOrderReceived)
  }

  fun onOrderReceived(order: Order) {
    log.info("Received order: $order")
  }
}
