package com.fResult.rsocket.integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.file.dsl.FileInboundChannelAdapterSpec
import org.springframework.integration.file.dsl.Files
import org.springframework.integration.scheduling.PollerMetadata
import java.io.File

@Configuration
class FileInboundConfiguration {
  @Bean
  fun fileIn(@Value("\${user.home}") home: File): FileInboundChannelAdapterSpec =
    Files.inboundAdapter(File(home, "in")).autoCreateDirectory(true)

  @Bean(PollerMetadata.DEFAULT_POLLER_METADATA_BEAN_NAME)
  fun defaultPoller(): PollerMetadata = Pollers.fixedRate(100).`object`
}
