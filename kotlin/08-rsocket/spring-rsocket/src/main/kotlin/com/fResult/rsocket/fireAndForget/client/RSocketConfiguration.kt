package com.fResult.rsocket.fireAndForget.client

import com.fResult.rsocket.FResultProperties
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester

@Configuration
class RSocketConfiguration {
  companion object {
    private val log: Logger = LogManager.getLogger(RSocketConfiguration::class.java)
  }

  @Bean
  fun rSocketRequester(
    props: FResultProperties,
    builder: RSocketRequester.Builder,
  ): RSocketRequester {
    log.info("RSocketRequester: {}:{}", props.rsocket.hostname, props.rsocket.port)

    return builder
      .dataMimeType(MediaType.APPLICATION_JSON)
      .tcp(props.rsocket.hostname, props.rsocket.port)
  }
}
