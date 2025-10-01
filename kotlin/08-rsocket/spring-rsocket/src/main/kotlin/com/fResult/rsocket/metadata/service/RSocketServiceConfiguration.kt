package com.fResult.rsocket.metadata.service

import com.fResult.rsocket.metadata.Constants
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.codec.Decoder
import org.springframework.core.codec.StringDecoder
import org.springframework.messaging.rsocket.MetadataExtractorRegistry

@Configuration
class RSocketServiceConfiguration {
  @Bean
  fun rSocketStrategiesCustomizer() = RSocketStrategiesCustomizer { strategiesBuilder ->
    strategiesBuilder
      .metadataExtractorRegistry(::withClientContextMetadata)
      .decoders(::withStringDecoders)
  }

  private fun withClientContextMetadata(registry: MetadataExtractorRegistry) {
    registry.metadataToExtract(Constants.CLIENT_ID, String::class.java, Constants.CLIENT_ID_HEADER)
    registry.metadataToExtract(Constants.LANG, String::class.java, Constants.LANG_HEADER)
  }

  private fun withStringDecoders(decoders: MutableList<Decoder<*>>) {
    decoders.add(StringDecoder.allMimeTypes())
  }
}
