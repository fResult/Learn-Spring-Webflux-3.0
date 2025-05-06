package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.common.utils.DataSourceUtils
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

class DevelopmentOnlyCustomerService : BaseCustomerService(buildDataSource()) {
  companion object {
    private fun buildDataSource(): DataSource {
      val dataSource = EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()

      return DataSourceUtils.initializeDdl(dataSource)
    }
  }
}
