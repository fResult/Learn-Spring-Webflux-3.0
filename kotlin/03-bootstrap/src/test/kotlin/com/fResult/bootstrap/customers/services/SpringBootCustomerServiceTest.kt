package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.TransactionTestMixin
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringBootCustomerServiceTest : BaseClass(), TransactionTestMixin {
  @Autowired
  private lateinit var customerService: CustomerService

  override fun getCustomerService(): CustomerService = customerService

  @Test
  override fun `should insert customer`() {
    super.`should insert customer`()
    testTransactionalityOfSave(getCustomerService())
  }
}
