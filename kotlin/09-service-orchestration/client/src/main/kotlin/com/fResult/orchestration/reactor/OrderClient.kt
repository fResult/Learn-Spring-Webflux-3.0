package com.fResult.orchestration.reactor

import com.fResult.orchestration.Order
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class OrderClient(private val http: WebClient) {
  fun getOrders(vararg ids: Int): Flux<Order> {
    val ordersRoot = "http://order-service/orders?customer-ids=${StringUtils.arrayToDelimitedString(ids.toTypedArray(), ",")}"

    return http.get().uri(ordersRoot).retrieve().bodyToFlux(Order::class.java)
  }
}
