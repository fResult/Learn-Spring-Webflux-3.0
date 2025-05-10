package com.fResult.bootstrap.customers.controllers

import com.fResult.bootstrap.customers.Customer
import com.fResult.bootstrap.customers.services.CustomerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(val customerService: CustomerService) {
  @GetMapping
  fun all() = customerService.findAll()

  @GetMapping("/{id}")
  fun byId(@PathVariable id: Long): Customer? = customerService.findById(id)

  @PostMapping
  fun create(@RequestBody body: Customer): Collection<Customer> = customerService.save(body.name)
}
