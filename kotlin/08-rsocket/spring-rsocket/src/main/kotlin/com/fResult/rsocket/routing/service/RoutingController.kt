package com.fResult.rsocket.routing.service

import com.fResult.rsocket.routing.Customer
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class RoutingController {
  private val customers = mapOf<Int, Customer>(1 to Customer(1, "John Wick"), 2 to Customer(2, "John Constantine"))

  @MessageMapping("customers")
  fun all(): Flux<Customer> = Flux.fromStream(customers.values.stream())

  @MessageMapping("customers.{id}")
  fun byId(@DestinationVariable id: Int) = Mono.justOrEmpty(customers[id])
}
