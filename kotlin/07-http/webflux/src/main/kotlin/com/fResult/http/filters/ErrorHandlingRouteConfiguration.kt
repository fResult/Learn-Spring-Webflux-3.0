package com.fResult.http.filters

import com.fResult.http.customers.Customer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class ErrorHandlingRouteConfiguration {
  @Bean
  fun errors(): RouterFunction<ServerResponse> = coRouter {
    GET("/errors").nest {
      GET("products/{id}", ::findProductById)
      GET("customers/{id}", ::findCustomerById)
    }
  }.filter { request, next ->
    next.handle(request).onErrorResume(ElementNotFoundException::class.java) { ex ->
      ServerResponse.notFound().build()
    }
  }

  private suspend fun findProductById(request: ServerRequest): ServerResponse {
    val productId = request.pathVariable("id")

    return if (setOf("1", "2").contains(productId)) {
      throw ProductNotFoundException("Product with ID [$productId] not found")
    } else {
      ServerResponse.ok().bodyValueAndAwait(Product(productId))
    }
  }

  private suspend fun findCustomerById(request: ServerRequest): ServerResponse {
    val customerId = request.pathVariable("id")

    return if (setOf("1", "2").contains(customerId)) {
      throw CustomerNotFoundException("Customer with ID [$customerId] not found")
    } else {
      ServerResponse.ok().bodyValueAndAwait(Customer(customerId, "Customer Name $customerId"))
    }
  }
}

data class Product(val id: String)

class ProductNotFoundException : ElementNotFoundException {
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}

class CustomerNotFoundException : ElementNotFoundException {
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}

open class ElementNotFoundException : RuntimeException {
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}
