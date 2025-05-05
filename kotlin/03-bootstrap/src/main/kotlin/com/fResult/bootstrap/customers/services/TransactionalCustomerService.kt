package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.customers.Customer
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource

@Service
@Primary
class TransactionalCustomerService(dataSource: DataSource) : BaseCustomerService(dataSource) {
  @Transactional
  override fun save(vararg names: String): Collection<Customer> = super.save(*names)

  @Transactional
  override fun findById(id: Long): Customer? = super.findById(id)

  @Transactional
  override fun findAll(): Collection<Customer> = super.findAll()
}
