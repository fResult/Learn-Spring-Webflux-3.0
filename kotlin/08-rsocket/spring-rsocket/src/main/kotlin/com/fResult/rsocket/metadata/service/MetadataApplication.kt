package com.fResult.rsocket.metadata.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MetadataApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  System.setProperty("spring.profiles.active", "service")

  runApplication<MetadataApplication>(*args)
}
