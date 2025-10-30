package com.fResult.orchestration

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@RestController
class CustomerRestController(
  @Value($$"${delay:2000}")
  private val delayInMillis: Int,
) {
  private val customers: Map<Int, Customer> = mapOf(
    1 to "Jan",
    2 to "Mia",
    3 to "Leroy",
    4 to "Badhr",
    5 to "Zhen",
    6 to "Juliette",
    7 to "Artem",
    8 to "Michelle",
    9 to "Eva",
    10 to "Richard",
  ).entries.associate(::toCustomerById)

  @GetMapping("/customers")
  fun customers(
    @RequestParam(required = false) ids: Array<Int>?,
    @RequestParam(required = false) delay: Boolean,
  ): Flux<Customer> {
    val source = ids
      ?.asSequence()
      ?.distinct()
      ?.sorted()
      ?.mapNotNull(customers::get)
      ?.let(::mapToCustomerFlux)
      ?: Flux.fromStream(customers.values.stream())

    return if (delay) source.delaySubscription(delayInMillis.milliseconds.toJavaDuration())
    else source
  }

  private fun toCustomerById(entry: Map.Entry<Int, String>) =
    entry.key to Customer(entry.key, entry.value)

  private fun mapToCustomerFlux(customers: Sequence<Customer>): Flux<Customer> = Flux.fromIterable(customers.toList())
}
