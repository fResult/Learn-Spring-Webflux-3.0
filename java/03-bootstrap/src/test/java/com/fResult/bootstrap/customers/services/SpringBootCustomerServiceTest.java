package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.TransactionTestMixin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootCustomerServiceTest extends BaseClass implements TransactionTestMixin {
  @Autowired private CustomerService customerService;

  @Override
  public CustomerService getCustomerService() {
    return customerService;
  }

  @Test
  @Override
  public void insert() {
    super.insert();

    testTransactionalityOfSave(getCustomerService());
  }
}
