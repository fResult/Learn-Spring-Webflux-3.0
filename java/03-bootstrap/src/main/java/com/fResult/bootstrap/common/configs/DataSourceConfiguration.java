package com.fResult.bootstrap.common.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfiguration {
  private DataSourceConfiguration() {
    throw new IllegalStateException("This cannot be instantiated");
  }

  @Configuration
  @Profile("prod")
  @PropertySource("application-prod.yml")
  public static class ProductionConfiguration {
    @Bean
    DataSource dataSource(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.driver-class-name}") Class<?> driverClass) {

      final var dataSource = new DriverManagerDataSource(url, username, password);
      dataSource.setDriverClassName(driverClass.getName());

      return dataSource;
    }
  }
}
