package com.fResult.orchestration.resilience4j

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResilientClientApplication

fun main(args: Array<String>) {
  runApplication<ResilientClientApplication>(*args)
}
