package com.fResult.bootstrap.customers.services;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class ComponentScannedCustomerService extends TransactionTemplateCustomerService {
  public ComponentScannedCustomerService(
      DataSource dataSource, TransactionTemplate transactionTemplate) {

    super(dataSource, transactionTemplate);
  }
}
