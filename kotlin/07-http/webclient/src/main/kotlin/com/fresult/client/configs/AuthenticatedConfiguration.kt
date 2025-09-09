package com.fresult.client.configs

import com.fresult.client.AuthenticatedClient
import com.fresult.client.ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AuthenticatedConfiguration {
  @Bean
  fun authenticatedClient(builder: WebClient.Builder, props: ClientProperties): AuthenticatedClient {
    val httpProps = props.http
    val basicAuthProps = httpProps.basic

    val filterFunction = ExchangeFilterFunctions.basicAuthentication(
      basicAuthProps.username ?: "",
      basicAuthProps.password ?: ""
    )

    return builder.baseUrl(httpProps.rootUrl)
      .filter(filterFunction)
      .build()
      .let { AuthenticatedClient(it) }
  }
}
