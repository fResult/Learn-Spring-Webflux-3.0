package com.fResult.bootstrap.customers.services

import javax.sql.DataSource

class DataSourceCustomerService(ds: DataSource) : BaseCustomerService(ds)
