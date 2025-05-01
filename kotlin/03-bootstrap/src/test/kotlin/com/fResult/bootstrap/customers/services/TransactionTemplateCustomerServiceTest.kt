package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.TransactionTestMixin
import com.fResult.bootstrap.common.DataSourceUtils
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.support.TransactionTemplate

class TransactionTemplateCustomerServiceTest : BaseClass(), TransactionTestMixin {

  private lateinit var customerService: CustomerService

  @Test
  override fun `should insert customer`() {
    super.`should insert customer`()
    testTransactionalityOfSave(getCustomerService())
  }

  override fun getCustomerService(): CustomerService = EmbeddedDatabaseBuilder()
    .setType(EmbeddedDatabaseType.H2)
    .build()
    .let {
      val dataSource = DataSourceUtils.initializeDdl(it)
      val txnManager = DataSourceTransactionManager(dataSource)
      val transactionTemplate = TransactionTemplate(txnManager)

      TransactionTemplateCustomerService(dataSource, transactionTemplate)
    }.also { customerService = it }
}
