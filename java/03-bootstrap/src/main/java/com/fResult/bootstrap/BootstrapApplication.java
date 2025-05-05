package com.fResult.bootstrap;

import com.fResult.bootstrap.common.utils.SpringUtils;
import com.fResult.bootstrap.common.configs.DataSourceConfiguration;
import com.fResult.bootstrap.customers.services.CustomerService;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@ComponentScan
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
  TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
    return new TransactionTemplate(transactionManager);
  }
}
