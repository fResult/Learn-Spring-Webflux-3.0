package com.fResult.orchestration.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiGatewayApplication


fun main(args: Array<String>) {
  System.setProperty("server.port", "8080")
  runApplication<ApiGatewayApplication>(*args)
}
