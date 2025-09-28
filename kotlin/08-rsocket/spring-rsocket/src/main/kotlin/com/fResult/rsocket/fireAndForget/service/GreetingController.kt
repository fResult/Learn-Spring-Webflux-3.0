package com.fResult.rsocket.fireAndForget.service

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class GreetingController {
  companion object {
    private val log: Logger = LogManager.getLogger(GreetingController::class.java)
  }

  @MessageMapping("greeting")
  fun greetName(name: String): Unit = log.info("New command sent to update the name: {}", name)
}
