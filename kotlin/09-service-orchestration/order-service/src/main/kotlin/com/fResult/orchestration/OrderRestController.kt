package com.fResult.orchestration

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

typealias CustomerId = Int

@RestController
@RequestMapping("/orders")
class OrderRestController {
  private val customerIdToOrders = ConcurrentHashMap<CustomerId, List<Order>>().apply {
    (0 until 10).forEach { id ->
      val list = CopyOnWriteArrayList<Order>().apply {
        val orderAmount = ((Math.random() * 10).toInt()).coerceAtLeast(1)
        repeat(orderAmount) {
          add(Order(UUID.randomUUID().toString(), id))
        }
      }
      put(id, list)
    }
  }

  @GetMapping
  fun orders(@RequestParam(required = false, name = "customer-ids") customerIds: Array<Int>): Flux<Order> {
    val customerIdStream = customerIdToOrders.keys.stream()
    val includedCustomerIds = customerIds.toList()
    val orderStream = customerIdStream.filter(includedCustomerIds::contains)
      .flatMap { id -> customerIdToOrders[id]?.stream() }

    return Flux.fromStream(orderStream)
  }
}
