package com.fResult.bootstrap.customers.services;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class DataSourceCustomerServiceTest extends BaseClass {
  private final CustomerService customerService;

  DataSourceCustomerServiceTest() {
    final var dataSource =
        new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .build();

    customerService = new DataSourceCustomerService(dataSource);
  }

  @Override
  public CustomerService getCustomerService() {
    return customerService;
  }
}
