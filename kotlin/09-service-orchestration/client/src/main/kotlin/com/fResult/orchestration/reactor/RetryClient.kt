package com.fResult.orchestration.reactor

import com.fResult.orchestration.Order
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.util.Loggers

@Component
class RetryClient(private val orderClient: OrderClient) {
  companion object {
    private val log = Loggers.getLogger(TimeoutClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    orderClient.getOrders(1, 2)
      .retry(10)
      .subscribe(::onOrderReceived)
  }

  private fun onOrderReceived(order: Order) {
    log.info("Received order: $order")
  }
}
