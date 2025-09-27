package com.fResult.rsocket.channel.service

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class PongController {
  companion object {
    val log: Logger = LogManager.getLogger(PongController::class.java)
  }

  @MessageMapping("pong")
  fun pong(@Payload ping: Flux<String>): Flux<String> = ping.map { "pong" }
    .doOnNext(log::info)
}
