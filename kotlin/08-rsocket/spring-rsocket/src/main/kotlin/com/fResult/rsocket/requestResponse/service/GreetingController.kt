package com.fResult.rsocket.requestResponse.service

import com.fResult.rsocket.FResultProperties
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class GreetingController(val props: FResultProperties) {
  companion object {
    private val log: Logger = LogManager.getLogger(GreetingController::class.java)
  }

  @MessageMapping("greeting")
  fun greet(@Headers headers: Map<String, Any>, @Payload name: String): Mono<String> {
    headers.forEach { (key, value) -> log.info("Header: $key = $value") }

    return Mono.just("Hello, $name!")
  }
}
