package com.fResult.rsocket.errorHandling.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ErrorHandlingApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<ErrorHandlingApplication>(*args)
  Thread.currentThread().join()
}
