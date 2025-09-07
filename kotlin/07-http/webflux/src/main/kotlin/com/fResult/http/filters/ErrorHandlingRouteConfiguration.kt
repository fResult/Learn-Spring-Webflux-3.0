package com.fResult.http.filters

import com.fResult.common.ExceptionProblemResponseMapper
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
    next.handle(request).onErrorResume(ExceptionProblemResponseMapper::map)
  }

  // Simulated handler functions that may throw exceptions
  private suspend fun findProductById(request: ServerRequest): ServerResponse {
    val productId = request.pathVariable("id")

    // Simulate some error conditions for product
    return if (productId == "404") {
      throw ProductNotFoundException("Product with ID [$productId] not found")
    } else if (productId == "0") {
      throw ProductOutOfStockException("Product with ID [$productId] is out of stock")
    } else {
      // Simulate a successful product retrieval
      ServerResponse.ok().bodyValueAndAwait(Product(productId))
    }
  }

  // Simulated handler functions that may throw exceptions
  private suspend fun findCustomerById(request: ServerRequest): ServerResponse {
    val customerId = request.pathVariable("id")

    // Simulate some error conditions for customer
    return if (customerId == "404") {
      throw CustomerNotFoundException("Customer with ID [$customerId] not found")
    } else if (customerId == "999") {
      throw RuntimeException("Unexpected error occurred while retrieving customer with ID [$customerId]")
    } else {
      // Simulate a successful customer retrieval
      ServerResponse.ok().bodyValueAndAwait(Customer(customerId, "Customer Name $customerId"))
    }
  }
}

data class Product(val id: String)

class ProductOutOfStockException : RuntimeException {
  constructor(message: String) : super(message)
  constructor(cause: Throwable) : super(cause)
  constructor(message: String, cause: Throwable) : super(message, cause)
}

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
