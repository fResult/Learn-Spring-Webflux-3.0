package com.fResult.rsocket.encoding.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EncodingApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "service")

  runApplication<EncodingApplication>(*args)
}
