package com.fResult.rsocket.requestResponse.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RequestResponseApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<RequestResponseApplication>(*args)
}
