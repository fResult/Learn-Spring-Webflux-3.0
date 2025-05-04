package com.fResult.bootstrap.customers.services

import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

@Service
class ComponentScannedCustomerService(
  dataSource: DataSource,
  transactionTemplate: TransactionTemplate,
) : TransactionTemplateCustomerService(dataSource, transactionTemplate)
