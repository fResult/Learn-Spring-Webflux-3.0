package com.fResult.orchestration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(SlowServiceProperties::class)
class SlowServiceApplication

fun main(args: Array<String>) {
  runApplication<SlowServiceApplication>(*args)
}
