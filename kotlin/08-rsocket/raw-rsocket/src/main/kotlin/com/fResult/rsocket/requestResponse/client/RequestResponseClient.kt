package com.fResult.rsocket.requestResponse.client

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
import reactor.util.retry.Retry
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Component
class RequestResponseClient(private val props: FResultProperties) {
  companion object {
    private val log: Logger = LogManager.getLogger(RequestResponseClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting {} connection", RequestResponseClient::class.simpleName)

    val clientTransport = TcpClientTransport.create(props.rsocket.hostname, props.rsocket.port)
    val rSocketSource = RSocketConnector.create()
      .reconnect(Retry.backoff(50, 500.milliseconds.toJavaDuration()))
      .connect(clientTransport)

    val client = RSocketClient.from(rSocketSource)

    client.requestResponse(Mono.just(DefaultPayload.create("Reactive Spring")))
      .repeat(10)
      .doOnNext(::logAndReleasePayload)
      .blockLast()
  }

  private fun logAndReleasePayload(payload: Payload) {
    log.info("Received response data: {}", payload.dataUtf8)
    payload.release()
  }
}
