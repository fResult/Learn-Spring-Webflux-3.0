package com.fResult.http.customers

import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

typealias CustomerId = String

@Repository
class CustomerRepository {
  private val customers = ConcurrentHashMap<CustomerId, Customer>()

  fun findAll(): Flux<Customer> = Flux.fromIterable(customers.values)

  fun findById(id: CustomerId): Mono<Customer> = Mono.justOrEmpty(customers.get(id))

  fun save(customer: Customer): Mono<Customer> = Mono.defer {
    Mono.just(customer)
      .map { it.copy(id = it.id ?: UUID.randomUUID().toString()) }
      .doOnNext {
        val id = requireNotNull(it.id) { "ID must not be null" }
        customers[id] = it
      }
  }
}
