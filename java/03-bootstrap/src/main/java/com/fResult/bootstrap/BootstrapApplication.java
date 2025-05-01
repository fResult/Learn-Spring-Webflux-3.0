package com.fResult.bootstrap;

import com.fResult.bootstrap.customers.services.DevelopmentOnlyCustomerService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootstrapApplication {
  public static void main(String[] args) {
    final var customerService = new DevelopmentOnlyCustomerService();
    Demo.workWithCustomerService(BootstrapApplication.class, customerService);
  }
}
