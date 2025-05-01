package com.fResult.bootstrap.customers.services

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

class DataSourceCustomerServiceTest() : BaseClass() {
  private val customerService: CustomerService

  init {
    EmbeddedDatabaseBuilder()
      .setType(EmbeddedDatabaseType.H2)
      .addScript("classpath:schema.sql")
      .build()
      .also { customerService = DataSourceCustomerService(it) }
  }

  override fun getCustomerService(): CustomerService {
    return customerService
  }
}
