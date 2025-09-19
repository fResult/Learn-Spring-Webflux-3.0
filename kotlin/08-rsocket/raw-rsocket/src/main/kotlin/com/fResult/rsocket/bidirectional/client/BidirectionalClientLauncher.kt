package com.fResult.rsocket.bidirectional.client

import com.fResult.rsocket.EncodingUtils
import com.fResult.rsocket.FResultProperties
import com.fResult.rsocket.bidirectional.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.stream.IntStream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class BidirectionalClientLauncher(
  private val props: FResultProperties,
  private val encodingUtils: EncodingUtils,
) {
  companion object {
    val log: Logger = LogManager.getLogger(BidirectionalClientLauncher::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val maxClients = (5..10).random()
    val hostname = props.rsocket.hostname
    val port = props.rsocket.port
    log.info("Launching {} clients connecting to {}:{}", maxClients, hostname, port)

    Flux.fromStream(IntStream.range(0, maxClients).boxed())
      .map { id -> BidirectionalClient(encodingUtils, id.toString(), hostname, port) }
      .flatMap { client -> Flux.just(client).delayElements((1..30).random().seconds.toJavaDuration()) }
      .flatMap(BidirectionalClient::getGreetings)
      .subscribe(::logGreetingResponse)
  }

  private fun logGreetingResponse(greeting: GreetingResponse?): Unit {
    greeting?.also { log.info(it.message) }
      ?: log.warn("Received null GreetingResponse")
  }
}
