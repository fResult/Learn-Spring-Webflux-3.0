package com.fResult.orchestration.reactor

import com.fResult.orchestration.Order
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.util.Loggers
import reactor.util.retry.Retry
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class RetryWhenClient(private val orderClient: OrderClient) {
  companion object {
    val log = Loggers.getLogger(RetryWhenClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    orderClient.getOrders(1, 2, 7)
      .retryWhen(Retry.backoff(10, 1.seconds.toJavaDuration()))
      .subscribe(::onOrderReceived)
  }

  private fun onOrderReceived(order: Order) {
    log.info("Received order: $order")
  }
}
