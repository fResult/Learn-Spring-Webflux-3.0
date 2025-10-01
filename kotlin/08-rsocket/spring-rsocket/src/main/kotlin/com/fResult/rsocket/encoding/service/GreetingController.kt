package com.fResult.rsocket.encoding.service

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class GreetingController {
  companion object {
    private val log: Logger = LogManager.getLogger(GreetingController::class.java)
  }

  @MessageMapping("greetings")
  fun greet(@Payload body: GreetingRequest, @Headers headers: Map<String, Any>): Mono<GreetingResponse> {
    headers.forEach { key, value -> log.info("Greeting header: {} -> {}", key, value) }

    return Mono.just(GreetingResponse("Hello, ${body.name}!") )
  }
}

