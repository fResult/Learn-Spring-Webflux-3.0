package com.fResult.orchestration.hedging.client

import com.fResult.orchestration.Order
import com.fResult.orchestration.hedging.qualifier.LoadBalancedWebClient
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.util.Loggers

@Component
class OrderClient(@LoadBalancedWebClient private val http: WebClient) {
  companion object {
    private val log = Loggers.getLogger(OrderClient::class.java)
  }

  fun getOrders(vararg ids: Int): Flux<Order> {
    val ordersRoot = "http://order-service/orders?ids=${StringUtils.arrayToDelimitedString(ids.toTypedArray(), ",")}"

    return http.get().uri(ordersRoot).retrieve().bodyToFlux(Order::class.java)
      .onErrorResume { ex ->
        log.error("Failed to fetch orders: {}", ex.message, ex)
        Flux.empty()
      }
  }
}
