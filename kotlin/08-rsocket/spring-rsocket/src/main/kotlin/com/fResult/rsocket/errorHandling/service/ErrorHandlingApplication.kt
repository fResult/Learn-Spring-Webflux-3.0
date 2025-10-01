package com.fResult.rsocket.errorHandling.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ErrorHandlingApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "service")

  runApplication<ErrorHandlingApplication>(*args)
}
