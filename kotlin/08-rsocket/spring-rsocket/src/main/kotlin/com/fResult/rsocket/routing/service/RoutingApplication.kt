package com.fResult.rsocket.routing.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RoutingApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "service")

  runApplication<RoutingApplication>(*args)
}
