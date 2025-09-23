package com.fResult.rsocket.metadata.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MetadataApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<MetadataApplication>(*args)
  Thread.currentThread().join()
}
