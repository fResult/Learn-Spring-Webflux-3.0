package com.fResult.rsocket.requestResponse.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RequestResponseApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<RequestResponseApplication>(*args)
  Thread.currentThread().join()
}
