package com.fResult.bootstrap.customers.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
@Primary
@Transactional
public class TransactionalCustomerService extends BaseCustomerService {
  public TransactionalCustomerService(DataSource dataSource) {
    super(dataSource);
  }
}
