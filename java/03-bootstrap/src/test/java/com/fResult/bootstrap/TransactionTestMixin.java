package com.fResult.bootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fResult.bootstrap.customers.services.CustomerService;
import org.slf4j.LoggerFactory;

public interface TransactionTestMixin {
  default void testTransactionalityOfSave(CustomerService customerService) {
    final var log = LoggerFactory.getLogger(getClass());
    log.info("using customer {}", customerService.getClass().getSimpleName());

    final var expectedResult = customerService.findAll();
    final var expectedCount = expectedResult.size();

    try {
      customerService.save("Anderson", null);
    } catch (Exception ex) {
      final var actualResult = customerService.findAll();
      assertEquals(
          expectedCount,
          actualResult.size(),
          "there should be no new records in the database: " + ex.getMessage());
      return;
    }

    fail("Expected exception was not thrown");
  }
}
