package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.BootstrapApplication;

public class ContextCustomerServiceTest extends ApplicationContextAwareBaseClass {
  @Override
  protected Class<?> getConfigurationClass() {
    return BootstrapApplication.class;
  }
}
