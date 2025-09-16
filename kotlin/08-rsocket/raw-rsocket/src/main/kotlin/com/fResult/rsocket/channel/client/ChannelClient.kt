package com.fResult.rsocket.channel.client

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
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class ChannelClient(private val props: FResultProperties) {
  companion object {
    private val log: Logger = LogManager.getLogger(ChannelClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting {} connection", ChannelClient::class.simpleName)

    val clientTransport = TcpClientTransport.create(props.rsocket.hostname, props.rsocket.port)
    val socketSource = RSocketConnector.create()
      .reconnect(Retry.backoff(50, 500.milliseconds.toJavaDuration()))
      .connect(clientTransport)

    RSocketClient.from(socketSource)
      .requestChannel(Flux.interval(1.seconds.toJavaDuration()).map(::toHelloPayload))
      .map(Payload::getDataUtf8)
      .take(10)
      .subscribe(::logReceivedResponse)
  }

  fun toHelloPayload(elapsedIdx: Long) = DefaultPayload.create("Hello #$elapsedIdx")

  fun logReceivedResponse(message: String) {
    log.info("Received response data: {}", message)
  }
}
