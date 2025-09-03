package com.fResult.r2dbc

import com.fResult.r2dbc.repositories.common.SimpleCustomerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier

@Testcontainers
abstract class BaseCustomerServiceTest {
  abstract fun getCustomerRepository(): SimpleCustomerRepository

  private lateinit var customerRepository: SimpleCustomerRepository

  @MockitoBean
  private lateinit var customerService: CustomerService

  companion object {
    @JvmStatic
    @DynamicPropertySource
    fun registerProperties(registry: org.springframework.test.context.DynamicPropertyRegistry) {
      registry.add("spring.sql.init.mode") { "always" }
      registry.add("spring.r2dbc.url") { "r2dbc:tc:postgresql://rsbhost/rsb?TC_IMAGE_TAG=9.6.8" }
    }
  }

  @BeforeEach
  fun reset() {
    customerRepository = getCustomerRepository()
  }

  @Test
  fun badUpsert() {
    StepVerifier.create(customerRepository.findAll().flatMap { customerRepository.deleteById(it.id!!) })
      .verifyComplete()

    val badEmail = "bad-email"
    val firstWrite = customerService.upsert(badEmail).thenMany(customerRepository.findAll())

    StepVerifier.create(firstWrite).expectError().verify()
    StepVerifier.create(customerRepository.findAll()).expectNextCount(0).verifyComplete()
  }
}