package com.fResult.rsocket.setup.service

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class SetupController {
  companion object {
    val log: Logger = LogManager.getLogger(SetupController::class.java)
  }

  @MessageMapping("greetings.{name}")
  fun hello(@DestinationVariable name: String): Mono<String> = Mono.just("Hello $name!")

  @Suppress("UNUSED")
  @ConnectMapping("setup")
  fun setup(@Payload setupPayload: String, @Headers headers: Map<String, Any>) {
    log.info("Received setup payload: {}", setupPayload)
    headers.forEach { key, value -> log.info("Headers: $key->$value") }
  }
}
