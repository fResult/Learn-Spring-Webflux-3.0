package com.fResult.bootstrap.common.utils

import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

class DataSourceUtils private constructor() {
  companion object {
    fun initializeDdl(dataSource: DataSource): DataSource {
      val populator = ResourceDatabasePopulator(ClassPathResource("/schema.sql"))
      DatabasePopulatorUtils.execute(populator, dataSource)

      return dataSource
    }
  }
}

