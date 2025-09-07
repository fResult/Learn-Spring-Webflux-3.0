package com.fResult.http.filters

import com.fResult.http.customers.Customer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
class ErrorHandlingRouteConfiguration {
  @Bean
  fun errors(): RouterFunction<ServerResponse> = coRouter {
    GET("/errors").nest {
      GET("products/{id}", ::findProductById)
      GET("customers/{id}", ::findCustomerById)
    }
  }.filter { request, next ->
    next.handle(request).onErrorResume(::exceptionToProblemDetailResponse)
  }

  private fun exceptionToProblemDetailResponse(ex: Throwable): Mono<ServerResponse> {
    return when (ex) {
      is ElementNotFoundException -> buildStatusToProblemDetail(HttpStatus.NOT_FOUND, ex, "Not found")
      is ProductOutOfStockException -> buildStatusToProblemDetail(HttpStatus.CONFLICT, ex, "Out of stock")
      else -> buildStatusToProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex)
    }.let { (status, problem) ->
      return ServerResponse.status(status).bodyValue(problem)
    }
  }

  private fun buildStatusToProblemDetail(
    status: HttpStatus,
    ex: Throwable,
    defaultMessage: String = "Something went wrong",
  ): Pair<HttpStatus, ProblemDetail> =
    status to ProblemDetail.forStatusAndDetail(status, ex.message ?: defaultMessage)


  private suspend fun findProductById(request: ServerRequest): ServerResponse {
    val productId = request.pathVariable("id")

    // Simulate some error conditions for product
    return if (setOf("1", "2").contains(productId)) {
      throw ProductNotFoundException("Product with ID [$productId] not found")
    } else if (productId == "9") {
      throw ProductOutOfStockException("Product with ID [$productId] is out of stock")
    } else {
      // Simulate a successful product retrieval
      ServerResponse.ok().bodyValueAndAwait(Product(productId))
    }
  }

  private suspend fun findCustomerById(request: ServerRequest): ServerResponse {
    val customerId = request.pathVariable("id")

    // Simulate some error conditions for customer
    return if (setOf("1", "2").contains(customerId)) {
      throw CustomerNotFoundException("Customer with ID [$customerId] not found")
    } else if (customerId == "9") {
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
