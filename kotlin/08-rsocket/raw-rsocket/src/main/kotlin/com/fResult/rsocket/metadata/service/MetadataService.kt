package com.fResult.rsocket.metadata.service

import com.fResult.rsocket.EncodingUtils
import com.fResult.rsocket.FResultProperties
import com.fResult.rsocket.metadata.Constants
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.transport.netty.server.TcpServerTransport
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MetadataService(
  private val props: FResultProperties,
  private val encodingUtils: EncodingUtils,
) {
  companion object {
    val log: Logger = LogManager.getLogger(MetadataService::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val serverTransport = TcpServerTransport.create(props.rsocket.hostname, props.rsocket.port)

    val createRSocketServer: (SocketAcceptor) -> RSocketServer = RSocketServer::create

    SocketAcceptor.with(metadataPushHandler())
      .let(createRSocketServer)
      .bind(serverTransport)
      .doOnNext(::logStartup)
      .block()
  }

  private fun metadataPushHandler(): RSocket = object : RSocket {
    override fun metadataPush(payload: Payload): Mono<Void> {
      val metadata = payload.metadataUtf8.let(encodingUtils::decodeMetadata)
      val clientId = metadata[Constants.CLIENT_ID_HEADER] as String
      val stringBuilder = StringBuilder()
        .append(System.lineSeparator())
        .append("($clientId) ---------------------------------")
        .append(System.lineSeparator())

      metadata.forEach { key, value ->
        stringBuilder.append("($clientId) $key = $value").append(System.lineSeparator())
      }
      log.info(stringBuilder)

      return Mono.empty()
    }
  }

  private fun logStartup(channel: CloseableChannel) {
    log.info("Server started on the address: {}", channel.address())
  }
}
