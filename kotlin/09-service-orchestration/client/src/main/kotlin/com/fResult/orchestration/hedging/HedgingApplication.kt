package com.fResult.orchestration.hedging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HedgingApplication

fun main(args: Array<String>) {
  runApplication<HedgingApplication>(*args)
}
