package com.fResult.rsocket.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecurityApplication

fun main(args: Array<String>) {
	System.setProperty("spring.profiles.active", "service")
	runApplication<SecurityApplication>(*args)
}
