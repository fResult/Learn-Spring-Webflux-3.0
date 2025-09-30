package com.fResult.rsocket.encoding.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EncodingApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<EncodingApplication>(*args)
  Thread.currentThread().join()
}
