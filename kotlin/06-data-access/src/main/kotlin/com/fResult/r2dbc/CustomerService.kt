package com.fResult.r2dbc

import com.fResult.r2dbc.repositories.SimpleCustomerRepository
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CustomerService(
  private val repository: SimpleCustomerRepository,
  private val operator: TransactionalOperator,
  private val initializer: CustomerDatabaseInitializer,
) {
  fun resetDatabase(): Publisher<Void> = initializer.resetCustomerTable()

  fun upsert(email: String): Flux<Customer> {
    return repository.findAll()
      .filter(sameCustomerEmailIgnoreCase(email))
      .flatMap { repository.update(Customer(it.id, email)) }
      .switchIfEmpty(repository.save(Customer(null, email)))
      .filter(::validateEmail)
      .switchIfEmpty(displayIllegalArgumentError())
      .let(operator::transactional)
  }

  @Transactional
  fun normalizeEmails(): Flux<Customer> =
    repository.findAll()
      .flatMap { upsert(it.email.uppercase()) }
      .filter(::validateEmail)
      .switchIfEmpty(displayIllegalArgumentError())

  private fun sameCustomerEmailIgnoreCase(email: String): (Customer) -> Boolean = { customer ->
    customer.email.equals(email, ignoreCase = true)
  }

  private fun validateEmail(customer: Customer): Boolean = customer.email.contains("@")

  private fun displayIllegalArgumentError(): Mono<Customer> =
    Mono.error(IllegalArgumentException("The email needs to be of the form [email@example.com]!"))
}
