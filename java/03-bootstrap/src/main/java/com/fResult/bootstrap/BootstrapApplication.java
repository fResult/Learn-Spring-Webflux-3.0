package com.fResult.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @Configuration
// @ComponentScan
// @EnableTransactionManagement
// @Import(DataSourceConfiguration.class)
@SpringBootApplication
public class BootstrapApplication {
  public static void main(String... args) {
    System.setProperty("spring.profiles.active", "prod");
    SpringApplication.run(BootstrapApplication.class, args);
  }

  /* NO-SONAR java:S125
   * public static void main(String[] args) {
   *   final var context = SpringUtils.run(BootstrapApplication.class, "prod");
   *
   *   final var customerService = context.getBean(CustomerService.class);
   *   Demo.workWithCustomerService(BootstrapApplication.class, customerService);
   * }
   *
   * @Bean
   * public PlatformTransactionManager transactionManager(DataSource dataSource) {
   *   return new DataSourceTransactionManager(dataSource);
   * }
   *
   * @Bean
   * public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
   *   return new TransactionTemplate(transactionManager);
   * }
   */
}
