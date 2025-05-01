package com.fResult.bootstrap.customers.services;

class DevelopmentOnlyCustomerServiceTest extends BaseClass {
  private final CustomerService customerService;

  public DevelopmentOnlyCustomerServiceTest() {
    this.customerService = new DevelopmentOnlyCustomerService();
  }

  @Override
  public CustomerService getCustomerService() {
    return this.customerService;
  }
}
