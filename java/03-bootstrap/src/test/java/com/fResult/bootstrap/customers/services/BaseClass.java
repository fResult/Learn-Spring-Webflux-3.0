package com.fResult.bootstrap.customers.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public abstract class BaseClass {
  public abstract CustomerService getCustomerService();

  @Test
  public void insert() {
    final var actualResult = getCustomerService().save("Wick");

    assertNotNull(actualResult);
    assertEquals(1, actualResult.size());
  }

  @Test
  public void getAllCustomers() {
    Stream.of("A", "B").forEach(getCustomerService()::save);

    assertTrue(getCustomerService().findAll().size() > 2);
  }

  @Test
  public void getById() {
    final var id = getCustomerService().save("A").iterator().next().id();

    assertEquals(id, getCustomerService().findById(id).id());
  }
}
