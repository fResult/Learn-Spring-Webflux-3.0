package com.fResult.bootstrap;

import com.fResult.bootstrap.common.DataSourceUtils;
import com.fResult.bootstrap.customers.services.CustomerService;
import com.fResult.bootstrap.customers.services.TransactionTemplateCustomerService;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootApplication
public class BootstrapApplication {
  public static void main(String[] args) {
    final var dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();

    Optional.of(dataSource)
        .map(BootstrapApplication::buildCustomerService)
        .ifPresent(BootstrapApplication::workWithCustomerServiceConsumer);
  }

  private static CustomerService buildCustomerService(DataSource dataSource) {
    final var initializedDataSource = DataSourceUtils.initializeDdl(dataSource);
    final var dsTxManager = new DataSourceTransactionManager(initializedDataSource);
    final var transactionTemplate = new TransactionTemplate(dsTxManager);

    return new TransactionTemplateCustomerService(initializedDataSource, transactionTemplate);
  }

  private static void workWithCustomerServiceConsumer(CustomerService customerService) {
    Demo.workWithCustomerService(BootstrapApplication.class, customerService);
  }
}
