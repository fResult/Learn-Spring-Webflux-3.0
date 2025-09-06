package com.fResult.r2dbc.configs

import com.fResult.r2dbc.repositories.common.CommonCustomerRepository
import com.fResult.r2dbc.repositories.common.SimpleCustomerRepository
import com.fResult.r2dbc.repositories.springdata.SpringDataCustomerRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.r2dbc.core.DatabaseClient
import com.fResult.r2dbc.repositories.springdata.CustomerRepository as R2dbcCustomerRepository

@Configuration
class CustomerRepositoryConfiguration {
  @Bean
  @Profile("common")
  fun commonCustomerRepository(
    dbClient: DatabaseClient,
    environment: Environment,
  ): SimpleCustomerRepository = CommonCustomerRepository(dbClient, environment)

  @Bean
  @Profile("springdata")
  fun springDataCustomerRepository(
    repository: R2dbcCustomerRepository,
    environment: Environment,
  ): SimpleCustomerRepository = SpringDataCustomerRepository(repository, environment)
}
