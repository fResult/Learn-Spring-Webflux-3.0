package com.fResult.bootstrap.customers.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class BaseClass {
  private static CustomerService customerService;

  public abstract CustomerService getCustomerService();

  @BeforeEach
  void setUp() {
    customerService = getCustomerService();
  }

  @Test
  public void insert() {
    final var actualResult = customerService.save("Wick");

    assertNotNull(actualResult);
    assertEquals(1, actualResult.size());
  }

  @Test
  public void getAllCustomers() {
    Stream.of("A", "B").forEach(customerService::save);

    assertTrue(customerService.findAll().size() > 2);
  }

  @Test
  public void getById() {
    final var id = customerService.save("A").iterator().next().id();

    assertEquals(id, getCustomerService().findById(id).id());
  }
}
