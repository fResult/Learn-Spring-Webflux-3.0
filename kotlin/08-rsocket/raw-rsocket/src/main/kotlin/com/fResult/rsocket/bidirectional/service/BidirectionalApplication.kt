package com.fResult.rsocket.bidirectional.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BidirectionalApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<BidirectionalApplication>(*args)
  Thread.currentThread().join()
}
