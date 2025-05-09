package com.fResult.bootstrap.common.configs;

import com.fResult.bootstrap.common.factories.YmlPropertySourceFactory;
import com.fResult.bootstrap.common.utils.DataSourceUtils;
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
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

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
        @Value("${spring.datasource.driver-class-name}") Class<?> driverClass) {

      final var dataSource = new DriverManagerDataSource(url, username, password);
      dataSource.setDriverClassName(driverClass.getName());

      return dataSource;
    }
  }

  @Configuration
  @Profile("default")
  @PropertySource(value = "application-default.yml", factory = YmlPropertySourceFactory.class)
  public static class DevelopmentConfiguration {
    @Bean
    DataSource dataSource() {
      return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
  }

  private static class DataSourcePostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nonnull String beanName)
        throws BeansException {

      return switch (bean) {
        case EmbeddedDatabaseBuilder embeddedDbBuilder ->
            DataSourceUtils.initializeDdl(
                embeddedDbBuilder.setType(EmbeddedDatabaseType.H2).build());

        case DataSource dataSource -> DataSourceUtils.initializeDdl(dataSource);
        default -> bean;
      };
    }
  }
}
