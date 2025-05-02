package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.customers.Customer;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionTemplateCustomerService extends BaseCustomerService {
  private final TransactionTemplate transactionTemplate;

  public TransactionTemplateCustomerService(
      DataSource dataSource, TransactionTemplate transactionTemplate) {

    super(dataSource);
    this.transactionTemplate = transactionTemplate;
  }

  @Override
  public Collection<Customer> save(String... names) {
    return transactionTemplate.execute(ignored -> super.save(names));
  }

  @Override
  public Customer findById(Long id) {
    return transactionTemplate.execute(ignored -> super.findById(id));
  }

  @Override
  public Collection<Customer> findAll() {
    return transactionTemplate.execute(ignored -> super.findAll());
  }
}
