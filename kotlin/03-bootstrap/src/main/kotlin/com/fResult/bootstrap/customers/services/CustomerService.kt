package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.customers.Customer

interface CustomerService {
  fun save(vararg names: String): Collection<Customer>

  fun findById(id: Long): Customer?

  fun findAll(): Collection<Customer>
}
