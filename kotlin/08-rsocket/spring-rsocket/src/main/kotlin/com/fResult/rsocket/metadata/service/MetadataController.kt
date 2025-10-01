package com.fResult.rsocket.metadata.service

import com.fResult.rsocket.metadata.Constants
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class MetadataController {
  companion object {
    private val log: Logger = LogManager.getLogger(MetadataController::class.java)
  }

  @Suppress("UNUSED")
  @ConnectMapping
  fun setup(@Headers metadata: Map<String, Any>): Mono<Void> {
    log.info("## Setup")
    return enumerate(metadata)
  }

  @MessageMapping("message")
  fun message(
    @Header(Constants.CLIENT_ID_HEADER) clientId: String,
    @Headers metadata: Map<String, Any>,
  ): Mono<Void> {
    log.info("## Message for {} {}", Constants.CLIENT_ID_HEADER, clientId)

    return enumerate(metadata)
  }

  private fun enumerate(headers: Map<String, Any>): Mono<Void> {
    headers.forEach { header, value -> log.info("## $header -> $value") }

    return Mono.empty();
  }
}
