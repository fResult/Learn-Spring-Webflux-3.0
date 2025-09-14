package com.fResult.rsocket.fireAndForget.service

import com.fResult.rsocket.FResultProperties
import io.rsocket.Payload
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
import reactor.netty.tcp.TcpServer

@Component
class FireAndForgetService(private val props: FResultProperties) {
  companion object {
    val log: Logger = LogManager.getLogger(FireAndForgetService::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val serverTransport =
      TcpServer.create()
        .host(props.rsocket.hostname)
        .port(props.rsocket.port)
        .let(TcpServerTransport::create)

    val rSocketServer = SocketAcceptor.forFireAndForget(::fireAndForgetHandler)
      .let(RSocketServer::create)

    rSocketServer.bind(serverTransport)
      .doOnNext(::logStartup)
      .block()
  }

  private fun fireAndForgetHandler(payload: Payload): Mono<Void> =
    payload.also { log.info("Received request data: {}", it.dataUtf8) }.let { Mono.empty() }

  private fun logStartup(channel: CloseableChannel) {
    log.info("Server started on the address: {}", channel.address())
  }
}
