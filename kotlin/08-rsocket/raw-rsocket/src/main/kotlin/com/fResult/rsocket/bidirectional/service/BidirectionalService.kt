package com.fResult.rsocket.bidirectional.service

import com.fResult.rsocket.EncodingUtils
import com.fResult.rsocket.FResultProperties
import com.fResult.rsocket.bidirectional.ClientHealthState
import com.fResult.rsocket.bidirectional.GreetingRequest
import com.fResult.rsocket.bidirectional.GreetingResponse
import io.rsocket.ConnectionSetupPayload
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.CloseableChannel
import io.rsocket.transport.netty.server.TcpServerTransport
import io.rsocket.util.DefaultPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.stream.Stream
import kotlin.math.max
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
class BidirectionalService(
  private val props: FResultProperties,
  private val encodingUtils: EncodingUtils,
) {
  companion object {
    val log: Logger = LogManager.getLogger(BidirectionalService::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val serverTransport = TcpServerTransport.create(props.rsocket.hostname, props.rsocket.port)
    val socketServer = RSocketServer.create(::socketAcceptor)

    socketServer.bind(serverTransport).doOnNext(::logStartup).block()
  }

  private fun socketAcceptor(setup: ConnectionSetupPayload, sendingSocket: RSocket) =
    Mono.just(createRequestStreamHandler(sendingSocket))

  private fun createRequestStreamHandler(clientRSocket: RSocket): RSocket {
    return object : RSocket {
      override fun requestStream(payload: Payload): Flux<Payload> =
        streamUntilClientStop(clientRSocket, payload)
    }
  }

  private fun streamUntilClientStop(clientRSocket: RSocket, payload: Payload): Flux<Payload> {
    val clientHealthStream = clientRSocket.requestStream(DefaultPayload.create(ByteArray(0)))
      .map(payloadToObject(ClientHealthState::class))
      .filter(::isClientHealthStateStopped)

    val replyPayloads = Flux.fromStream(Stream.generate(greetingResponseSupplierFrom(payload)))
      .delayElements(max(3, (Math.random() * 10).toInt()).seconds.toJavaDuration())
      .doFinally { signalType -> log.info("Finished.") }

    return replyPayloads.takeUntilOther(clientHealthStream)
      .map(encodingUtils::encode)
      .map(DefaultPayload::create)
  }

  private fun <T : Any> payloadToObject(klass: KClass<T>): (Payload) -> T =
    { payload -> encodingUtils.decode(payload.dataUtf8, klass) }

  private fun isClientHealthStateStopped(chs: ClientHealthState) =
    ClientHealthState.STOPPED.equals(chs.state, ignoreCase = true)

  private fun greetingResponseSupplierFrom(payload: Payload): () -> GreetingResponse = {
    val greetingRequest = payload.let(payloadToObject(GreetingRequest::class))
    val message = "Hello, ${greetingRequest.name} @ ${Instant.now()}"

    GreetingResponse(message)
  }

  private fun logStartup(channel: CloseableChannel): Unit =
    log.info("Server started on the address: {}", channel.address())
}
