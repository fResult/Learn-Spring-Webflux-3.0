package com.fResult.r2dbc

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class R2dbcApplication

fun main(args: Array<String>) {
  runApplication<R2dbcApplication>(*args)
}

@Component
class PrintConfig(
  private val props: R2dbcProperties,
) : ApplicationRunner {
  override fun run(args: ApplicationArguments?) {
    println(">>> R2DBC URL: ${props.url}, user: ${props.username}")
  }
}
