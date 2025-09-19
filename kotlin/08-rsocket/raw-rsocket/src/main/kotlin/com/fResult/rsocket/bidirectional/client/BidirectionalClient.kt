package com.fResult.rsocket.bidirectional.client

import com.fResult.rsocket.EncodingUtils
import com.fResult.rsocket.bidirectional.ClientHealthState
import com.fResult.rsocket.bidirectional.GreetingRequest
import com.fResult.rsocket.bidirectional.GreetingResponse
import io.rsocket.ConnectionSetupPayload
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.util.DefaultPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class BidirectionalClient(
  private val encodingUtils: EncodingUtils,
  private val uid: String,
  private val serviceHostname: String,
  private val servicePort: Int,
) {
  companion object {
    val log: Logger = LogManager.getLogger(BidirectionalClient::class.java)
  }

  fun getGreetings(): Flux<GreetingResponse?> {
    val greetingRequestPayload = encodingUtils.encode(GreetingRequest("Client #$uid"))

    return RSocketConnector.create()
      .acceptor(::acceptor)
      .connect(TcpClientTransport.create(serviceHostname, servicePort))
      .flatMapMany { socket ->
        socket.requestStream(DefaultPayload.create(greetingRequestPayload))
          .map { payload -> encodingUtils.decode(payload.dataUtf8, GreetingResponse::class) }
      }
  }

  private fun acceptor(setup: ConnectionSetupPayload, serverRSocket: RSocket): Mono<RSocket> =
    Mono.just(createRequestStreamHandler())

  private fun createRequestStreamHandler(): RSocket =
    object : RSocket {
      override fun requestStream(payload: Payload): Flux<Payload> {
        val start = Instant.now().toEpochMilli()
        val delayMillis = (0..30_000).random().toLong()

        val stateFlux = Flux.fromStream(Stream.generate {
          val now = Instant.now().toEpochMilli()
          val stop = ((start + delayMillis) < now) && Math.random() > .8

          ClientHealthState(if (stop) "STOPPED" else "STARTED")
        })
          .delayElements(5.seconds.toJavaDuration())

        return stateFlux
          .map(encodingUtils::encode)
          .map(DefaultPayload::create)
      }


      private fun logStartup(channel: CloseableChannel) {
        log.info("Server started on the address: {}", channel.address())
      }
    }
}
