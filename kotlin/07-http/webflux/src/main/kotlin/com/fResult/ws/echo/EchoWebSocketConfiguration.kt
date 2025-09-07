package com.fResult.ws.echo

import com.fResult.utils.IntervalMessageProducer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import reactor.core.publisher.SignalType
import reactor.core.scheduler.Schedulers

@Configuration
class EchoWebSocketConfiguration {
  companion object {
    private val log: Logger = LogManager.getLogger(EchoWebSocketConfiguration::class.java)
  }

  @Bean
  fun echoHm(): HandlerMapping = SimpleUrlHandlerMapping(mapOf("/ws/echo" to echoWebSocketHandler()), 10)

  @Bean
  fun echoWebSocketHandler(): WebSocketHandler = WebSocketHandler { session ->
    val outbound = IntervalMessageProducer.produce()
      .doOnNext(log::info)
      .map(session::textMessage)
      .doFinally { signalType -> log.info("outbound connection: {}", signalType) }

    val inbound = session.receive()
      .map(WebSocketMessage::getPayloadAsText)
      .publishOn(Schedulers.boundedElastic())
      .doFinally { signalType ->
        log.info("Inbound connection: {}", signalType)
        if (signalType == SignalType.ON_COMPLETE) {
          log.info("Closing session...")
          session.close().subscribe()
        }
      }.doOnNext(log::info)

    session.send(outbound).and(inbound)
  }
}
