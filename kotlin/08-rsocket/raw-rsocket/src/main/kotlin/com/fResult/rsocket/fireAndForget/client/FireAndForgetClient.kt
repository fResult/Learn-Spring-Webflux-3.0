package com.fResult.rsocket.fireAndForget.client

import com.fResult.rsocket.FResultProperties
import io.rsocket.Payload
import io.rsocket.core.RSocketClient
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.netty.tcp.TcpClient
import reactor.util.retry.Retry
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Component
class FireAndForgetClient(private val props: FResultProperties) {
  companion object {
    private val log: Logger = LogManager.getLogger(FireAndForgetClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting {} connection", FireAndForgetClient::class.simpleName)

    val clientTransport = TcpClient.create()
      .host(props.rsocket.hostname)
      .port(props.rsocket.port)
      .let(TcpClientTransport::create)

    val rSocketSource = RSocketConnector.create()
      .reconnect(Retry.backoff(50, 500.milliseconds.toJavaDuration()))
      .connect(clientTransport)

    val client = RSocketClient.from(rSocketSource)

    client.fireAndForget(Mono.just(DefaultPayload.create("Reactive Spring!")))
      .block()
  }
}
