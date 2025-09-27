package com.fResult.rsocket.channel.service

import com.fResult.rsocket.fp.then
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
  fun pong(@Payload ping: Flux<String>): Flux<String> =
    ping.doOnNext(::logIncomingPing).map(::toPongMessage).doOnNext(::logOutgoingPong)

  fun logIncomingPing(ping: String): Unit = log.info("Received request: {}", ping)
  fun logOutgoingPong(pong: String): Unit = log.info("Sending response: {}", pong)
  fun toPongMessage(request: String): String = request.let(::extractNumber then ::toPong)
  fun extractNumber(ping: String): Int = ping.substringAfter("Ping #").trim().toInt()
  fun toPong(number: Int): String = "Pong #$number"
}
