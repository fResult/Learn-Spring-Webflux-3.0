package com.fResult.r2dbc.repositories.common

import com.fResult.r2dbc.Customer
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
@Profile("common")
class CommonCustomerRepository(private val dbClient: DatabaseClient) : SimpleCustomerRepository {
  private fun map(row: Map<String, Any>) =
    Customer(
      id = (row["id"] as Int).toString(),
      email = row["email"] as String
    )

  override fun save(customer: Customer): Mono<Customer> =
    dbClient.sql("INSERT INTO customer ( email ) values ( :email )")
      .bind("email", customer.email)
      .filter { stmt, _ -> stmt.returnGeneratedValues("id").execute() }
      .fetch()
      .first()
      .flatMap { (it["id"] as Int).toString().let(::findById) }

  override fun findAll(): Flux<Customer> =
    dbClient.sql("SELECT * FROM customer").fetch().all().`as` { it.map(::map) }

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
      .map(::map)

  override fun deleteById(id: String): Mono<Void> {
    return dbClient.sql("DELETE FROM customer WHERE id = CAST(:id AS INTEGER)")
      .bind("id", id)
      .fetch()
      .rowsUpdated()
      .then()
  }
}
