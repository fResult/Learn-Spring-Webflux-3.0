package com.fResult.rsocket.bidirectional.client

import com.fResult.rsocket.FResultProperties
import io.rsocket.SocketAcceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler

@Configuration
class RSocketConfiguration {
  @Bean
  fun socketAcceptor(healthController: HealthController, strategies: RSocketStrategies): SocketAcceptor =
    RSocketMessageHandler.responder(strategies, healthController)

  @Bean
  fun rSocketRequester(
    socketAcceptor: SocketAcceptor,
    builder: RSocketRequester.Builder,
    properties: FResultProperties,
  ): RSocketRequester = builder
    .rsocketConnector { connector -> connector.acceptor(socketAcceptor) }
    .tcp(properties.rsocket.hostname, properties.rsocket.port)
}
