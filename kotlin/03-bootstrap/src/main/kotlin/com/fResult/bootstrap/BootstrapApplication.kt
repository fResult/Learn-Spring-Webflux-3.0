package com.fResult.bootstrap

import com.fResult.bootstrap.common.DataSourceUtils
import com.fResult.bootstrap.customers.services.DataSourceCustomerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

@SpringBootApplication
class BootstrapApplication

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
  EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
    .let(DataSourceUtils::initializeDdl)
    .let(::DataSourceCustomerService)
    .also { Demo.workWithCustomerService(BootstrapApplication::class, it) }
}
