package com.fResult.r2dbc.repositories.common

import com.fResult.r2dbc.Customer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("common")
class CommonCustomerRepository(
  private val dbClient: DatabaseClient,
  private val environment: Environment,
) : SimpleCustomerRepository {

  companion object {
    val log: Logger = LogManager.getLogger(CommonCustomerRepository::class.java)
  }

  private fun mapper(row: Map<String, Any>) =
    Customer(
      id = (row["id"] as Int).toString(),
      email = row["email"] as String
    )

  init {
    (environment.activeProfiles
      .takeIf(Array<out String>::isNotEmpty)
      ?.joinToString()
      ?: "common")
      .also { profile ->
        log.info(
          "[{}] initialized with profile: [{}]",
          CommonCustomerRepository::class.simpleName,
          profile,
        )
      }
  }

  override fun save(customer: Customer): Mono<Customer> =
    dbClient.sql("INSERT INTO customer ( email ) values ( :email )")
      .bind("email", customer.email)
      .filter { stmt, _ -> stmt.returnGeneratedValues("id").execute() }
      .fetch()
      .first()
      .flatMap { (it["id"] as Int).toString().let(::findById) }

  override fun findAll(): Flux<Customer> =
    dbClient.sql("SELECT * FROM customer").fetch().all().`as` { it.map(::mapper) }

  override fun update(customer: Customer): Mono<Customer> =
    dbClient.sql("UPDATE customer SET email = :email WHERE id = CAST(:id AS INTEGER)")
      .bind("id", customer.id!!)
      .bind("email", customer.email)
      .fetch()
      .first()
      .switchIfEmpty(Mono.empty())
      .then(findById(customer.id))

  override fun findById(id: String): Mono<Customer> =
    dbClient.sql("SELECT * FROM customer WHERE id = CAST(:id AS INTEGER)")
      .bind("id", id)
      .fetch()
      .first()
      .map(::mapper)

  override fun deleteById(id: String): Mono<Void> {
    return dbClient.sql("DELETE FROM customer WHERE id = CAST(:id AS INTEGER)")
      .bind("id", id)
      .fetch()
      .rowsUpdated()
      .then()
  }
}
