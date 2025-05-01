package com.fResult.bootstrap;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;

public class BaseCustomerService implements CustomerService {
  private final RowMapper<Customer> rowMapper =
      (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("NAME"));
  private final JdbcTemplate jdbcTemplate;

  public BaseCustomerService(DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  @Override
  public Collection<Customer> save(String... names) {
    final var customers = new ArrayList<Customer>();
    for (final var name : names) {
      final var keyHolder = new GeneratedKeyHolder();

      jdbcTemplate.update(
          connection -> {
            final var ps =
                connection.prepareStatement(
                    "INSERT INTO customers (name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);

            return ps;
          }, keyHolder);

      final var keyHolderKey = Objects.requireNonNull(keyHolder.getKey()).longValue();
      final var customer = findById(keyHolderKey);
      Assert.notNull(name, "the name given must not be null");
      customers.add(customer);
    }

    return customers;
  }

  @Override
  public Customer findById(Long id) {
    final var sql = "SELECT * FROM customers WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, rowMapper, id);
  }

  @Override
  public Collection<Customer> findAll() {
    return jdbcTemplate.query("SELECT * FROM customers", rowMapper);
  }
}
