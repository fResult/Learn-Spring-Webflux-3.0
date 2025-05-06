package com.fResult.bootstrap.customers.controllers;

import com.fResult.bootstrap.customers.Customer;
import com.fResult.bootstrap.customers.services.CustomerService;
import java.util.Collection;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping
  public Collection<Customer> all() {
    return customerService.findAll();
  }

  @GetMapping("/{id}")
  public Customer byId(@PathVariable Long id) {
    return customerService.findById(id);
  }

  @PostMapping
  public Collection<Customer> create(@RequestBody Customer body) {
    return customerService.save(body.name());
  }
}
