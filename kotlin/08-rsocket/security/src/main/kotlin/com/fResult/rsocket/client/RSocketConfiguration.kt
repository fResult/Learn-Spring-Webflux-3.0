package com.fResult.rsocket.client

import com.fResult.rsocket.FResultProperties
import io.rsocket.metadata.WellKnownMimeType
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata
import org.springframework.util.MimeTypeUtils

@Configuration
class RSocketConfiguration {
  @Bean
  fun rSocketRequester(
    props: FResultProperties,
    builder: RSocketRequester.Builder,
    credentials1: UsernamePasswordMetadata,
  ): RSocketRequester = builder
    .setupMetadata(credentials1, MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.string))
    .tcp(props.rsocket.hostname, props.rsocket.port)

  @Bean
  fun rSocketStrategiesCustomizer() = RSocketStrategiesCustomizer { strategies ->
    strategies.encoder(SimpleAuthenticationEncoder())
  }
}
