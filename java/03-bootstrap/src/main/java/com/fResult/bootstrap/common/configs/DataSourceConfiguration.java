package com.fResult.bootstrap.common.configs;

import com.fResult.bootstrap.common.DataSourceUtils;
import com.fResult.bootstrap.common.factories.YmlPropertySourceFactory;
import jakarta.annotation.Nonnull;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

@Configuration
public class DataSourceConfiguration {
  @Bean
  DataSourcePostProcessor dataSourcePostProcessor() {
    return new DataSourcePostProcessor();
  }

  @Configuration
  @Profile("prod")
  @PropertySource(value = "application-prod.yml", factory = YmlPropertySourceFactory.class)
  public static class ProductionConfiguration {
    @Bean
    DataSource dataSource(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.driver-class-name}") String driverClass) {

      final var dataSource = new DriverManagerDataSource(url, username, password);
      dataSource.setDriverClassName(driverClass);

      return dataSource;
    }
  }

  private static class DataSourcePostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nonnull String beanName)
        throws BeansException {

      return switch (bean) {
        case EmbeddedDatabaseBuilder dsBuilder -> DataSourceUtils.initializeDdl(dsBuilder.build());
        case DataSource dataSource -> DataSourceUtils.initializeDdl(dataSource);
        default -> bean;
      };
    }
  }
}
