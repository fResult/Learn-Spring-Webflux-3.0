package com.fResult.rsocket.setup.client

import com.fResult.rsocket.FResultProperties
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.util.MimeTypeUtils
import reactor.util.retry.Retry
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Configuration
class RSocketConfiguration {
  companion object {
    private val log: Logger = LogManager.getLogger(RSocketConfiguration::class.java)
  }

  @Bean
  fun rSocketRequester(properties: FResultProperties, builder: RSocketRequester.Builder): RSocketRequester {
    return builder.setupData("Setup data!")
      .setupRoute("setup")
      .rsocketConnector(withFixedDelayReconnect())
      .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
      .tcp(properties.rsocket.hostname, properties.rsocket.port)
  }

  private fun withFixedDelayReconnect(
    maxAttempts: Long = 2,
    backoff: Duration = 2.seconds,
  ) = RSocketConnectorConfigurer { connector ->
    connector.reconnect(Retry.fixedDelay(maxAttempts, backoff.toJavaDuration()))
  }
}
