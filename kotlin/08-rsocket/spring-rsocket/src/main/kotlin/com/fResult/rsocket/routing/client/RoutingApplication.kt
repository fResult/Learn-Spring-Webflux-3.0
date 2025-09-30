package com.fResult.rsocket.routing.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RoutingApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<RoutingApplication>(*args)
  Thread.currentThread().join()
}
