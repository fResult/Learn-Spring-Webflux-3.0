package com.fResult.r2dbc

import com.fResult.r2dbc.repositories.SimpleCustomerRepository
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.function.Supplier

@Testcontainers
abstract class BaseCustomerRepositoryTest {
//  @Autowired
//  abstract var initializer: CustomerDatabaseInitializer

  @DynamicPropertySource
  fun registerProperties(registry: DynamicPropertyRegistry) {
    registry.add("spring.sql.init.mode") { "always" }
    registry.add("spring.r2dbc.url") { "r2dbc:tc:postgresql://rsbhost/rsb?TC_IMAGE_TAG=9.6.8" }
  }

  abstract fun getRepository(): SimpleCustomerRepository

  @Test
  fun delete() {
    val repository = getRepository()
    val data = repository.findAll().flatMap { repository.deleteById(it.id!!) }.thenMany(
      Flux.just(
        Customer("first@email.com"),
        Customer("second@email.com"),
        Customer("third@email.com"),
      )
    ).flatMap(repository::save)

    StepVerifier.create(data).expectNextCount(3).verifyComplete()
    StepVerifier.create(repository.findAll().take(1).flatMap { repository.deleteById(it.id!!) }.then()).verifyComplete()
    StepVerifier.create(repository.findAll()).expectNextCount(2).verifyComplete()
  }
}