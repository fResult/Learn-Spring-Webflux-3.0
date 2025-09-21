package com.fResult.rsocket.bidirectional.client

import com.fResult.rsocket.EncodingUtils
import com.fResult.rsocket.FResultProperties
import com.fResult.rsocket.bidirectional.GreetingResponse
import com.fResult.rsocket.dsl.retry.RetryConfigBuilder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.nio.channels.ClosedChannelException
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
      .map(buildBidirectionalClient(encodingUtils, hostname, port))
      .flatMap(::toDelayClient)
      .flatMap(BidirectionalClient::getGreetings)
      .retryWhen(retryBackoffOnClosedChannel { maxAttempts = 5 })
      .subscribe(::logGreetingResponse)
  }

  private fun buildBidirectionalClient(
    encodingUtils: EncodingUtils,
    host: String,
    port: Int
  ): (Int) -> BidirectionalClient = { id ->
    BidirectionalClient(encodingUtils, id.toString(), host, port)
  }

  private fun toDelayClient(client: BidirectionalClient): Flux<BidirectionalClient> =
    Flux.just(client).delayElements((1..30).random().seconds.toJavaDuration())

  private fun retryBackoffOnClosedChannel(init: RetryConfigBuilder.() -> Unit): RetryBackoffSpec {
    val cfg = RetryConfigBuilder().apply(init).build()

    return Retry.backoff(cfg.maxAttempts, cfg.firstBackOff)
      .maxBackoff(cfg.maxBackoff)
      .filter { it is ClosedChannelException }
      .jitter(0.2)
      .onRetryExhaustedThrow { _, _ -> RuntimeException("Retries exhausted") }
  }

  private fun logGreetingResponse(greeting: GreetingResponse?): Unit {
    greeting?.apply { log.info(message) }
      ?: log.warn("Received null GreetingResponse")
  }
}
