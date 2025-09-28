package com.fResult.rsocket.bidirectional.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BidirectionalApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  System.setProperty("spring.rsocket.server.port", "9090");
  runApplication<BidirectionalApplication>(*args)
}
