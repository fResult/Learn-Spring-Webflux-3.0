package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.customers.Customer
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import java.sql.Statement
import javax.sql.DataSource

open class BaseCustomerService(ds: DataSource) : CustomerService {
  private val rowMapper = RowMapper { rs, _ -> Customer(rs.getLong("id"), rs.getString("NAME")) }
  private val jdbcTemplate = JdbcTemplate(ds)

  override fun save(vararg names: String): Collection<Customer> {
    val customers = ArrayList<Customer>()
    for (name in names) {
      val keyHolder = GeneratedKeyHolder()

      jdbcTemplate.update({ connection ->
        connection.prepareStatement("INSERT INTO customers (name) VALUES(?)", Statement.RETURN_GENERATED_KEYS)
          .also { it.setString(1, name) }
      }, keyHolder)

      keyHolder.key?.also { key ->
        val keyHolderKey = key.toLong()
        val customer = findById(keyHolderKey)

        customer?.also(customers::add)
      }
    }

    return customers
  }

  override fun findById(id: Long): Customer? {
    val sql = "SELECT * FROM customers WHERE id = ?"
    return jdbcTemplate.queryForObject(sql, rowMapper, id)
  }

  override fun findAll(): Collection<Customer> {
    return jdbcTemplate.query("SELECT * FROM customers", rowMapper)
  }
}
