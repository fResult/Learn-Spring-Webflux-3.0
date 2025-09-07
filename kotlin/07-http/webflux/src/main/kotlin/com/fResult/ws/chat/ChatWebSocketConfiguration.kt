package com.fResult.ws.chat

import com.fResult.utils.o
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.SignalType
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

@Configuration
class ChatWebSocketConfiguration(private val objectMapper: ObjectMapper) {
  val sessions = ConcurrentHashMap<String, Connection>()
  val messages = LinkedBlockingQueue<Message>()

  companion object {
    private val log: Logger = LogManager.getLogger(ChatWebSocketConfiguration::class.java)
  }

  @Bean
  fun chatHandlerMapping(): HandlerMapping = SimpleUrlHandlerMapping(mapOf("/ws/chat" to chatWebSocketHandler()), 2)

  @Bean
  fun chatWebSocketHandler(): WebSocketHandler {
    val messagesToBroadcast = Flux.create<Message> { sink ->
      val submit = Executors.newSingleThreadExecutor()
        .submit(broadcastIncomingMessages(sink))

      sink.onCancel { submit.cancel(true) }
    }.share()

    return buildChatSessionHandler(messagesToBroadcast)
  }

  fun buildChatSessionHandler(messagesToBroadcast: Flux<Message>): WebSocketHandler = WebSocketHandler { session ->
    val sessionID = session.id
    sessions[sessionID] = Connection(sessionID, session)
    log.info("New WebSocket connection: $sessionID, active sessions: ${sessions.size}")

    val messagesOffer: (Message) -> Boolean = messages::offer
    val inbound = session.receive()
      .doOnEach { log.info("WebSocket message received: {}", it) }
      .map(::jsonToMessage o WebSocketMessage::getPayloadAsText)
      .map(messagesOffer o toMessageWithMetadata(sessionID, Instant.now()))
      .doOnEach { log.info("messages: {}", messages) }
      .doFinally { signalType ->
        log.info("WebSocket connection closed: {}, signalType: {}", sessionID, signalType)
        if (signalType == SignalType.ON_COMPLETE) {
          sessions.remove(sessionID)
        }
      }

    val outbound = messagesToBroadcast
      .doOnEach { log.info("outgoing message: {}", it) }
      .map(session::textMessage o objectMapper::writeValueAsString)

    session.send(outbound).and(inbound)
  }

  private fun broadcastIncomingMessages(sink: FluxSink<Message>): () -> Unit = {
    while (!Thread.currentThread().isInterrupted) {
      try {
        // Turn external event source, `messages` into a reactive stream
        val message = messages.take()
        log.info("Broadcasting message: {}", message)
        sink.next(message)
      } catch (ex: InterruptedException) {
        Thread.currentThread().interrupt()
        throw RuntimeException(ex)
      }
    }
  }

  private fun jsonToMessage(json: String): Message {
    try {
      log.info("JSON IN: {}", json)
      val test = objectMapper.readValue(json, Message::class.java)
      log.info("Parsed message: {}", test)
      return test
    } catch (ex: Exception) {
      log.error("Failed to parse message: {}", json, ex)
      throw IllegalArgumentException("Invalid message format", ex)
    }
  }

  private fun toMessageWithMetadata(clientID: String, `when`: Instant): (Message) -> Message = { msg ->
    val updatedMsg = msg.copy(clientID, `when` = `when`)
    log.info("Adding metadata to message: {}", updatedMsg)
    updatedMsg
  }
}
