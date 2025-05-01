package com.fResult.bootstrap

import com.fResult.bootstrap.customers.Customer
import com.fResult.bootstrap.customers.services.CustomerService
import org.apache.logging.log4j.LogManager
import org.springframework.util.Assert
import java.util.function.Consumer
import kotlin.reflect.KClass

class Demo private constructor() {
  companion object {
    private val log = LogManager.getLogger(Demo::class.java)

    fun workWithCustomerService(
      label: KClass<BootstrapApplication>, customerService: CustomerService
    ) {
      log.info("====================================")
      log.info(label.simpleName)
      log.info("====================================")

      listOf("A", "B", "C")
        .map { customerService.save(it) }
        .forEach { customer -> log.info("saved {}", customer) }

      customerService
        .findAll()
        .forEach(
          Consumer { c: Customer ->
            val customer = customerService.findById(c.id)
            Assert.notNull(customer, "the resulting customer should not be null")
            Assert.isTrue(customer == c, "we should be able to query for " + "this result")
          })
    }
  }
}
