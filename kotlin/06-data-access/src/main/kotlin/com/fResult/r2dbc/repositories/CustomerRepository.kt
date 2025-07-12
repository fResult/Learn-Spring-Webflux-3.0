package com.fResult.r2dbc.repositories

import com.fResult.r2dbc.Customer
import com.fResult.r2dbc.repositories.SimpleCustomerRepository
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CustomerRepository(private val dbClient: DatabaseClient) : SimpleCustomerRepository {
  private fun map(row: Map<String, Any>) =
    Customer(
      id = (row["id"] as Number).toInt(),
      email = row["email"] as String
    )

  override fun save(customer: Customer): Mono<Customer> =
    dbClient.sql("INSERT INTO customer ( email ) values ( $1 )")
      .bind("$1", customer.email)
      .filter { stmt, _ -> stmt.returnGeneratedValues("id").execute() }
      .fetch()
      .first()
      .flatMap { row -> findById(row["id"] as Int) }

  override fun findAll(): Flux<Customer> =
    dbClient.sql("SELECT * FROM customer").fetch().all().`as` { it.map(::map) }

  override fun update(customer: Customer): Mono<Customer> =
    dbClient.sql("UPDATE customer SET email = $1 WHERE id = $2")
      .bind("$1", customer.email)
      .bind("$2", customer.id!!)
      .fetch()
      .first()
      .switchIfEmpty(Mono.empty())
      .then(findById(customer.id))

  override fun findById(id: Int): Mono<Customer> =
    dbClient.sql("SELECT * FROM customer WHERE id = $1")
      .bind("$1", id)
      .fetch()
      .first()
      .map(::map)

  override fun deleteById(id: Int): Mono<Void> {
    return dbClient.sql("DELETE FROM customer WHERE id = $1")
      .bind("$1", id)
      .fetch()
      .rowsUpdated()
      .then()
  }
}
