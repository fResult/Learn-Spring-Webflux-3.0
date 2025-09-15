package com.fResult.rsocket.requestResponse.service

import com.fResult.rsocket.FResultProperties
import io.rsocket.Payload
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.transport.netty.server.TcpServerTransport
import io.rsocket.util.DefaultPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RequestResponseService(private val props: FResultProperties) {
  companion object {
    val log: Logger = LogManager.getLogger(RequestResponseService::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val serverTransport = TcpServerTransport.create(props.rsocket.hostname, props.rsocket.port)

    val rSocketServer = SocketAcceptor.forRequestResponse(::requestResponseHandler)
      .let(RSocketServer::create)

    rSocketServer.bind(serverTransport)
      .doOnNext(::logStartup)
      .block()
  }

  private fun requestResponseHandler(payload: Payload): Mono<Payload> =
    payload.also { log.info("Received request data: {}", it.dataUtf8) }
      .let { Mono.just(DefaultPayload.create("Hello, ${it.dataUtf8}")) }

  private fun logStartup(channel: CloseableChannel) {
    log.info("Server started on the address: {}", channel.address())
  }
}
