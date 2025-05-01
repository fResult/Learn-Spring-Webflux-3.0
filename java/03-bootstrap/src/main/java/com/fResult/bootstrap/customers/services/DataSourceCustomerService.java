package com.fResult.bootstrap.customers.services;

import javax.sql.DataSource;

public class DataSourceCustomerService extends BaseCustomerService {
  public DataSourceCustomerService(DataSource ds) {
    super(ds);
  }
}
