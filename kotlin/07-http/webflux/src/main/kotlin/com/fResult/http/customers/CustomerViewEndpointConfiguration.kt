package com.fResult.http.customers

import kotlinx.coroutines.reactive.asFlow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class CustomerViewEndpointConfiguration(private val repository: CustomerRepository) {
  @Bean
  fun customersView(): RouterFunction<ServerResponse> = coRouter {
    GET("/fe/customers.php", ::customersViewHandler)
  }

  private suspend fun customersViewHandler(request: ServerRequest): ServerResponse {
    val modelMap = mapOf(
      "customers" to repository.findAll().asFlow(),
      "type" to "Functional Reactive Endpoint",
    )

    return ServerResponse.ok().renderAndAwait("customers", modelMap)
  }
}
