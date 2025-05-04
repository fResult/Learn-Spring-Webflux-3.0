package com.fResult.bootstrap

import com.fResult.bootstrap.common.SpringUtils
import com.fResult.bootstrap.common.configs.DataSourceConfiguration
import com.fResult.bootstrap.customers.services.CustomerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

@Configuration
@ComponentScan
@Import(DataSourceConfiguration::class)
class BootstrapApplication {
  @Bean
  fun transactionManager(dataSource: DataSource): PlatformTransactionManager = DataSourceTransactionManager(dataSource)

  @Bean
  fun transactionTemplate(transactionManager: PlatformTransactionManager) = TransactionTemplate(transactionManager)
}

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
  val context = SpringUtils.run(BootstrapApplication::class, "prod")
  val customerService = context.getBean(CustomerService::class.java)

  Demo.workWithCustomerService(BootstrapApplication::class, customerService)
}
