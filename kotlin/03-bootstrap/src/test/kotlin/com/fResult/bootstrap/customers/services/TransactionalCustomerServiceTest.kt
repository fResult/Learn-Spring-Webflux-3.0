package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.BootstrapApplication
import com.fResult.bootstrap.TransactionTestMixin
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

class TransactionalCustomerServiceTest : ApplicationContextAwareBaseClass(), TransactionTestMixin {
  @Test
  override fun `should insert customer`() {
    super.`should insert customer`()

    testTransactionalityOfSave(getCustomerService())
  }

  override fun getConfigurationClass(): KClass<BootstrapApplication> = BootstrapApplication::class
}
