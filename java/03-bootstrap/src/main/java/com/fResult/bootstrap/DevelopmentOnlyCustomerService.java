package com.fResult.bootstrap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

public class DevelopmentOnlyCustomerService extends BaseCustomerService {
  public DevelopmentOnlyCustomerService() {
    super(buildDataSource());
  }

  private static DataSource buildDataSource() {
    final var dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();

    return DataSourceUtils.initializeDdl(dataSource);
  }
}
