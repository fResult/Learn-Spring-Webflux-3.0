package com.fResult.bootstrap.customers.services

import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class SpringBootCustomerService(dataSource: DataSource) : TransactionalCustomerService(dataSource)
