package com.fResult.rsocket.setup.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SetupApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "service")

  runApplication<SetupApplication>(*args)
}
