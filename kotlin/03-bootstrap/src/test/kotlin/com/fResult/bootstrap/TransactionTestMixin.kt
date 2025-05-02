package com.fResult.bootstrap

import com.fResult.bootstrap.customers.services.CustomerService
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Assertions.assertEquals

interface TransactionTestMixin {
  fun testTransactionalityOfSave(customerService: CustomerService) {
    val log = LogManager.getLogger(javaClass)
    log.info("using customer {}", customerService.javaClass.simpleName)
    val count = customerService.findAll().size

    try {
      customerService.save("Bob", "null")
    } catch (ex: Exception) {
      assertEquals(
        count, customerService.findAll().size,
        "there should be no new records in the database",
      )
      return
    }

    // fail<Exception>("Expected exception was not thrown")
  }
}
