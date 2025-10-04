package com.fResult.rsocket.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecurityApplication

@Throws(InterruptedException::class)
fun main(args: Array<String>) {
	runApplication<SecurityApplication>(*args)
	Thread.currentThread().join()
}
