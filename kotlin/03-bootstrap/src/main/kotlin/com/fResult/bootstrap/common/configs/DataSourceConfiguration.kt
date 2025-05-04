package com.fResult.bootstrap.common.configs

import com.fResult.bootstrap.common.DataSourceUtils
import com.fResult.bootstrap.common.factories.YmlPropertySourceFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration {
  @Bean
  fun dataSourcePostProcessor(): DataSourcePostProcessor = DataSourcePostProcessor()

  @Configuration
  @Profile("prod")
  @PropertySource(
    value = ["application-prod.yml"],
    factory = YmlPropertySourceFactory::class
  )
  class ProductionConfiguration {
    @Bean
    fun dataSource(
      @Value("\${spring.datasource.url}") url: String,
      @Value("\${spring.datasource.username}") username: String,
      @Value("\${spring.datasource.password}") password: String,
      @Value("\${spring.datasource.driver-class-name}") driverClass: String,
    ): DataSource {
      val dataSource = DriverManagerDataSource(url, username, password)
      dataSource.setDriverClassName(driverClass)

      return dataSource
    }
  }

  @Configuration
  @Profile("default")
  @PropertySource(
    value = ["application-default.yml"],
    factory = YmlPropertySourceFactory::class
  )
  class DevelopmentConfiguration {
    @Bean
    fun dataSource() = EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
  }

  class DataSourcePostProcessor : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
      return when (bean) {
        is EmbeddedDatabaseBuilder ->
          DataSourceUtils.initializeDdl(bean.setType(EmbeddedDatabaseType.H2).build())

        is DataSource -> DataSourceUtils.initializeDdl(bean)
        else -> bean
      }
    }
  }
}
