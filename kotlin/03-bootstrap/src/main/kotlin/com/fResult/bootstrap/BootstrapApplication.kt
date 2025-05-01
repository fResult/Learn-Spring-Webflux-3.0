package com.fResult.bootstrap

import com.fResult.bootstrap.common.DataSourceUtils
import com.fResult.bootstrap.customers.services.TransactionTemplateCustomerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.support.TransactionTemplate

@SpringBootApplication
class BootstrapApplication

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
  EmbeddedDatabaseBuilder()
    .setType(EmbeddedDatabaseType.H2)
    .build()
    .let {
      val initializedDataSource = DataSourceUtils.initializeDdl(it)
      val dsTxManager = DataSourceTransactionManager(initializedDataSource)
      val transactionTemplate = TransactionTemplate(dsTxManager)

      TransactionTemplateCustomerService(initializedDataSource, transactionTemplate)
    }.also { Demo.workWithCustomerService(BootstrapApplication::class, it) }
}
