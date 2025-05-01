package com.fResult.bootstrap.customers.services

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class BaseClass {
  abstract fun getCustomerService(): CustomerService
  private lateinit var customerService: CustomerService

  @BeforeEach
  fun setUp() {
    customerService = getCustomerService()
  }

  @Test
  open fun `should insert customer`() {
    val actualResult = customerService.save("Wick")

    Assertions.assertNotNull(actualResult)
    Assertions.assertEquals(1, actualResult.size)
  }

  @Test
  fun `should retrieve all customers`() {
    listOf("A", "B").forEach { names ->
      customerService.save(names)
    }

    Assertions.assertTrue(customerService.findAll().size > 2)
  }

  @Test
  fun `should retrieve customer by id`() {
    val id = customerService.save("A").iterator().next().id

    customerService.findById(id)?.also { Assertions.assertEquals(id, it.id) }
  }
}

