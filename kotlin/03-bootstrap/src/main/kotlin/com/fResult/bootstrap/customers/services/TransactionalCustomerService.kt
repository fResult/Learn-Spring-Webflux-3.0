package com.fResult.bootstrap.customers.services

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.sql.DataSource

@Service
@Primary
@Transactional
class TransactionalCustomerService(dataSource: DataSource) : BaseCustomerService(dataSource)
