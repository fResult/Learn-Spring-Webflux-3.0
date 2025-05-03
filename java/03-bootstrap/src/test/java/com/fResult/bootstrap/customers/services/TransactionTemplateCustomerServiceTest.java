package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.TransactionTestMixin;
import com.fResult.bootstrap.common.DataSourceUtils;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;

class TransactionTemplateCustomerServiceTest extends BaseClass implements TransactionTestMixin {
  private CustomerService customerService;

  public TransactionTemplateCustomerServiceTest() {
    final var dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();

    Optional.of(dataSource).map(this::buildCustomerService).ifPresent(it -> customerService = it);
  }

  @Override
  @Test
  public void insert() {
    super.insert();
    this.testTransactionalityOfSave(getCustomerService());
  }

  @Override
  public CustomerService getCustomerService() {
    return customerService;
  }

  private CustomerService buildCustomerService(DataSource dataSource) {
    final var initializedDataSource = DataSourceUtils.initializeDdl(dataSource);
    final var txnManager = new DataSourceTransactionManager(initializedDataSource);
    final var transactionTemplate = new TransactionTemplate(txnManager);

    return new TransactionTemplateCustomerService(initializedDataSource, transactionTemplate);
  }
}
