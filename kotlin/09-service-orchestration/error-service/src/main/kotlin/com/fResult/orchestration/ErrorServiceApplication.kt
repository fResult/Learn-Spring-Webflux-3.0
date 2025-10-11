package com.fResult.orchestration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ErrorServiceApplication

fun main(args: Array<String>) {
  runApplication<ErrorServiceApplication>(*args)
}
