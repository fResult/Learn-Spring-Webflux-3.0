package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.customers.Customer;
import java.util.Collection;

public interface CustomerService {
  Collection<Customer> save(String... names);

  Customer findById(Long id);

  Collection<Customer> findAll();
}
