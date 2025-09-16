package com.fResult.rsocket.channel.service

import com.fResult.rsocket.FResultProperties
import io.rsocket.Payload
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.transport.netty.server.TcpServerTransport
import io.rsocket.util.DefaultPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.reactivestreams.Publisher
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ChannelService(private val props: FResultProperties) {
  companion object {
    val log: Logger = LogManager.getLogger(ChannelService::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val serverTransport = TcpServerTransport.create(props.rsocket.hostname, props.rsocket.port)
    val socketServer = SocketAcceptor.forRequestChannel(::requestChannelHandler)
      .let(RSocketServer::create)

    socketServer.bind(serverTransport).doOnNext(::logStartup).block()
  }

  private fun requestChannelHandler(payloads: Publisher<Payload>): Flux<Payload> =
    Flux.from(payloads)
      .doOnNext { log.info("Received request data: {}", it.dataUtf8) }
      .map { payload -> "Echo: ${payload.dataUtf8}" }
      .map(DefaultPayload::create)

  private fun logStartup(channel: CloseableChannel) {
    log.info("Server started on the address: {}", channel.address())
  }
}
