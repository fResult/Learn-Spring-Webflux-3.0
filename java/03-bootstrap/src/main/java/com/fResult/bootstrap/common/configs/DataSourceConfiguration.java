package com.fResult.bootstrap.common.configs;

import com.fResult.bootstrap.common.DataSourceUtils;
import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Configuration
public class DataSourceConfiguration {
  @Configuration
  @Profile("prod")
  @PropertySource(value = "application-prod.yml", factory = YamlPropertySourceFactory.class)
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

  @Bean
  DataSourcePostProcessor dataSourcePostProcessor() {
    return new DataSourcePostProcessor();
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

  private static class YamlPropertySourceFactory implements PropertySourceFactory {
    @Nonnull
    @Override
    public org.springframework.core.env.PropertySource<?> createPropertySource(
        @Nullable String name, @NonNull EncodedResource resource) throws IOException {
      YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
      factory.setResources(resource.getResource());
      Properties properties = factory.getObject();

      return Optional.ofNullable(resource.getResource().getFilename())
          .flatMap(buildPropertiesPropertySourceOpt(name, properties))
          .orElseThrow();
    }

    private Function<String, Optional<PropertiesPropertySource>> buildPropertiesPropertySourceOpt(
        @Nullable String name, @Nullable Properties properties) {

      return fileName ->
          Optional.ofNullable(properties).map(buildPropertiesPropertySource(name, fileName));
    }

    private Function<Properties, PropertiesPropertySource> buildPropertiesPropertySource(
        @Nullable String name, @Nonnull String fileName) {
      return props -> new PropertiesPropertySource(name != null ? name : fileName, props);
    }
  }
}
