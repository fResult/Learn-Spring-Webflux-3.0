package com.fResult.r2dbc

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@SpringBootApplication
class R2dbcApplication

fun main(args: Array<String>) {
  runApplication<R2dbcApplication>(*args)
}

@Component
class PrintConfig(
  @Value("\${spring.r2dbc.url}") private val url: String,
  @Value("\${spring.r2dbc.username}") private val username: String,
) : ApplicationRunner {
  override fun run(args: ApplicationArguments?) {
    println(">>> R2DBC URL: $url, user: $username")
  }
}
