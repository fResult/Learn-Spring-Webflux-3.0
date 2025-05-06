package com.fResult.bootstrap.customers.services;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class SpringBootCustomerService extends TransactionalCustomerService {
  public SpringBootCustomerService(DataSource dataSource) {
    super(dataSource);
  }
}
