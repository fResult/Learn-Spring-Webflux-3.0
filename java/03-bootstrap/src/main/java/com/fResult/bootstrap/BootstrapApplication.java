package com.fResult.bootstrap;

import com.fResult.bootstrap.common.SpringUtils;
import com.fResult.bootstrap.common.configs.DataSourceConfiguration;
import com.fResult.bootstrap.customers.services.CustomerService;
import com.fResult.bootstrap.customers.services.TransactionTemplateCustomerService;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@Import(DataSourceConfiguration.class)
public class BootstrapApplication {
  public static void main(String[] args) {
    final var context = SpringUtils.run(BootstrapApplication.class, "prod");

    final var customerService = context.getBean(CustomerService.class);
    Demo.workWithCustomerService(BootstrapApplication.class, customerService);
  }

  @Bean
  PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  TransactionTemplateCustomerService customerService(
      DataSource dataSource, TransactionTemplate transactionTemplate) {

    return new TransactionTemplateCustomerService(dataSource, transactionTemplate);
  }

  @Bean
  TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    return new TransactionTemplate(transactionManager);
  }
}
