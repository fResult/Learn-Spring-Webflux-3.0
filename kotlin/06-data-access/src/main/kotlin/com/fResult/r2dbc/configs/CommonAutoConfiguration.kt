package com.fResult.r2dbc.configs

import com.fResult.r2dbc.Customer
import com.fResult.r2dbc.CustomerDatabaseInitializer
import com.fResult.r2dbc.CustomerService
import com.fResult.r2dbc.repositories.common.SimpleCustomerRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class CommonAutoConfiguration {
  companion object {
    val log: Logger = LogManager.getLogger(CommonAutoConfiguration::class.java)
  }

  @Bean
  fun defaultCustomerService(
    customerRepository: SimpleCustomerRepository,
    transactionOperator: TransactionalOperator,
    dbInitializer: CustomerDatabaseInitializer,
  ): CustomerService {

    return CustomerService(customerRepository, transactionOperator, dbInitializer)
  }


  @Bean
  fun demo(repository: SimpleCustomerRepository): ApplicationListener<ApplicationReadyEvent> =
    ApplicationListener { event ->
      repository.save(Customer("fResult@exampl.com"))
        .thenMany(repository.findAll())
        .doOnEach(log::info)
        .blockFirst()
    }
}
