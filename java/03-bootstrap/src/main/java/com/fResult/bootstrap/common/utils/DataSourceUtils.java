package com.fResult.bootstrap.common.utils;

import javax.sql.DataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public final class DataSourceUtils {
  private DataSourceUtils() {
    throw new IllegalStateException("This cannot be instantiated");
  }

  public static DataSource initializeDdl(final DataSource dataSource) {
    final var populator = new ResourceDatabasePopulator(new ClassPathResource("/schema.sql"));
    DatabasePopulatorUtils.execute(populator, dataSource);

    return dataSource;
  }
}
