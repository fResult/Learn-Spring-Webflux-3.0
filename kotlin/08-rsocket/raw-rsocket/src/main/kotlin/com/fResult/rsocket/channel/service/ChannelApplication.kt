package com.fResult.rsocket.channel.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChannelApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
  runApplication<ChannelApplication>(*args)
  Thread.currentThread().join()
}
