package com.fResult.bootstrap.customers.services;

import com.fResult.bootstrap.common.utils.DataSourceUtils;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class DevelopmentOnlyCustomerService extends BaseCustomerService {
  public DevelopmentOnlyCustomerService() {
    super(buildDataSource());
  }

  private static DataSource buildDataSource() {
    final var dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();

    return DataSourceUtils.initializeDdl(dataSource);
  }
}
