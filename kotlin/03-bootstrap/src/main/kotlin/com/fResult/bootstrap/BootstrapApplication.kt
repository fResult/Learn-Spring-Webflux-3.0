package com.fResult.bootstrap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// @Configuration
// @ComponentScan
// @Import(DataSourceConfiguration::class)
@SpringBootApplication
class BootstrapApplication {
  /* NO-SONAR
   * @Bean
   * fun transactionManager(dataSource: DataSource): PlatformTransactionManager = DataSourceTransactionManager(dataSource)
   *
   * @Bean
   * fun transactionTemplate(transactionManager: PlatformTransactionManager) = TransactionTemplate(transactionManager)
   */
}

fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "prod")
  runApplication<BootstrapApplication>(*args)

  /* NO-SONAR
   * val context = SpringUtils.run(BootstrapApplication::class, "prod")
   * val customerService = context.getBean(CustomerService::class.java)
   *
   * Demo.workWithCustomerService(BootstrapApplication::class, customerService)
   */
}
