package com.fResult.bootstrap;

import com.fResult.bootstrap.common.DataSourceUtils;
import com.fResult.bootstrap.customers.services.DataSourceCustomerService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootApplication
public class BootstrapApplication {
  public static void main(String[] args) {
    final var dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    final var initializedDataSource = DataSourceUtils.initializeDdl(dataSource);

    final var customerService = new DataSourceCustomerService(initializedDataSource);

    Demo.workWithCustomerService(BootstrapApplication.class, customerService);
  }
}
