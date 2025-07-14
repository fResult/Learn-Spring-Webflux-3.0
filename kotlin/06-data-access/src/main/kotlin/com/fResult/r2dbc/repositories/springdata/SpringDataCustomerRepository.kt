package com.fResult.r2dbc.repositories.springdata

import com.fResult.r2dbc.Customer
import com.fResult.r2dbc.repositories.common.SimpleCustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("springdata")
class SpringDataCustomerRepository(
  private val repository: CustomerRepository,
  private val environment: Environment,
) : SimpleCustomerRepository {

  companion object {
    val log: Logger = LogManager.getLogger(SpringDataCustomerRepository::class.java)
  }

  private val rowMapper: (Map<String, Any>) -> Customer =
    { row -> Customer((row["id"] as Int).toString(), row["email"] as String) }

  init {
    (environment.activeProfiles
      .takeIf(Array<out String>::isNotEmpty)
      ?.joinToString()
      ?: "springdata")
      .also { profile ->
        log.info(
          "[{}] initialized with profile: [{}]",
          SpringDataCustomerRepository::class.simpleName,
          profile,
        )
      }
  }

  override fun save(customer: Customer): Mono<Customer> = repository.save(customer)

  override fun findAll(): Flux<Customer> = repository.findAll()

  override fun update(customer: Customer): Mono<Customer> = repository.save(customer)

  override fun findById(id: String): Mono<Customer> = repository.findById(id)

  override fun deleteById(id: String): Mono<Void> = repository.deleteById(id)
}
