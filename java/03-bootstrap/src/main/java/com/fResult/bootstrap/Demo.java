package com.fResult.bootstrap;

import com.fResult.bootstrap.customers.services.DevelopmentOnlyCustomerService;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

@Log4j2
public final class Demo {
  private Demo() {
    throw new IllegalStateException("This cannot be instantiated");
  }

  public static void workWithCustomerService(
      Class<BootstrapApplication> label, DevelopmentOnlyCustomerService customerService) {

    log.info("====================================");
    log.info(label.getName());
    log.info("====================================");

    Stream.of("A", "B", "C")
        .map(customerService::save)
        .forEach(customer -> log.info("saved " + customer));

    customerService
        .findAll()
        .forEach(
            c -> {
              final var customer = customerService.findById(c.id());
              Assert.notNull(customer, "the resulting customer should not be null");
              Assert.isTrue(customer.equals(c), "we should be able to query for " + "this result");
            });
  }
}
