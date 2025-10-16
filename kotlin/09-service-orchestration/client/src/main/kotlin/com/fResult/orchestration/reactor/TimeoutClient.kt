package com.fResult.orchestration.reactor

import com.fResult.orchestration.Order
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.util.Loggers
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class TimeoutClient(val orderClient: OrderClient) {
  companion object {
    private val log = Loggers.getLogger(TimeoutClient::class.java)
  }

   @EventListener(ApplicationReadyEvent::class)
   fun onApplicationReady() {
     orderClient.getOrders(1, 2)
       .timeout(10.seconds.toJavaDuration())
       .subscribe(::onOrderReceived)
   }

  private fun onOrderReceived(order: Order) {
    log.info("Received order: $order")
  }
}
