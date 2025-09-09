package com.fresult.client.configs

import com.fresult.client.ClientProperties
import com.fresult.client.DefaultClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class DefaultConfiguration {
  @Bean
  fun defaultClient(builder: WebClient.Builder, properties: ClientProperties): DefaultClient =
    properties.http.rootUrl
      .let { rootUrl ->
        DefaultClient(builder.baseUrl(rootUrl).build())
      }
}
