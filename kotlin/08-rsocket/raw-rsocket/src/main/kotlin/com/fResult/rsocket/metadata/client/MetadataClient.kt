package com.fResult.rsocket.metadata.client

import com.fResult.rsocket.EncodingUtils
import com.fResult.rsocket.FResultProperties
import com.fResult.rsocket.dsl.retry.retryBackoffOnClosedChannel
import com.fResult.rsocket.metadata.Constants
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
import java.util.*

@Component
class MetadataClient(
  private val props: FResultProperties,
  private val encodingUtils: EncodingUtils,
) {
  companion object {
    private val log: Logger = LogManager.getLogger(MetadataClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting {} connection", MetadataClient::class.simpleName)

    val rSocket = RSocketConnector.create()
      .reconnect(retryBackoffOnClosedChannel { maxAttempts = 5 } )
      .connect(TcpClientTransport.create(props.rsocket.hostname, props.rsocket.port))

    RSocketClient.from(rSocket)
      .metadataPush(Mono.just(buildMetadataUpdatePayload("fResult-client", Locale.ENGLISH)))
      .block()
  }

  private fun buildMetadataUpdatePayload(clientId: String, locale: Locale): Payload {
    val map = mapOf(
      Constants.CLIENT_ID_HEADER to clientId,
      Constants.LANG_HEADER to locale.language,
    )

    return DefaultPayload.create("", encodingUtils.encodeMetadata(map))
  }

  private fun logAndReleasePayload(payload: Payload) {
    log.info("Received response data: {}", payload.dataUtf8)
    payload.release()
  }
}
