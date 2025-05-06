package com.fResult.bootstrap;

import com.fResult.bootstrap.customers.services.CustomerService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DemoListener {
  private final CustomerService customerService;

  public DemoListener(CustomerService customerService) {
    this.customerService = customerService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void exercise() {
    Demo.workWithCustomerService(BootstrapApplication.class, customerService);
  }
}
