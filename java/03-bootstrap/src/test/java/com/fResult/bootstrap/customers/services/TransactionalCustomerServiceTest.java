package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.BootstrapApplication;
import com.fResult.bootstrap.TransactionTestMixin;
import org.junit.jupiter.api.Test;

class TransactionalCustomerServiceTest extends ApplicationContextAwareBaseClass
    implements TransactionTestMixin {

  @Test
  @Override
  public void insert() {
    super.insert();

    testTransactionalityOfSave(getCustomerService());
  }

  @Override
  protected Class<BootstrapApplication> getConfigurationClass() {
    return BootstrapApplication.class;
  }
}
