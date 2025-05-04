package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.customers.Customer
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

open class TransactionTemplateCustomerService(
  dataSource: DataSource,
  private val transactionTemplate: TransactionTemplate,
) : BaseCustomerService(dataSource) {

  override fun save(vararg names: String): Collection<Customer> =
    transactionTemplate.execute { super.save(*names) } ?: listOf()

  override fun findById(id: Long): Customer? = transactionTemplate.execute { super.findById(id) }

  override fun findAll(): Collection<Customer> = transactionTemplate.execute { super.findAll() } ?: listOf()
}
