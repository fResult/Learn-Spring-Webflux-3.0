package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.customers.Customer;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
public class TransactionalCustomerService extends BaseCustomerService {
  public TransactionalCustomerService(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  @Transactional
  public Collection<Customer> save(String... names) {
    return super.save(names);
  }

  @Override
  @Transactional
  public Customer findById(Long id) {
    return super.findById(id);
  }

  @Override
  @Transactional
  public Collection<Customer> findAll() {
    return super.findAll();
  }
}
