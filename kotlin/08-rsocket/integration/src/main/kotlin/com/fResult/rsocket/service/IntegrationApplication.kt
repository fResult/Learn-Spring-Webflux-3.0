package com.fResult.rsocket.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IntegrationApplication

fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "service")
  runApplication<IntegrationApplication>(*args)
}
