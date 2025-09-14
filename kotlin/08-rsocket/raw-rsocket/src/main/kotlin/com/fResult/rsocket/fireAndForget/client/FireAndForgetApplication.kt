package com.fResult.rsocket.fireAndForget.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FireAndForgetApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<FireAndForgetApplication>(*args)
  Thread.currentThread().join()
}
