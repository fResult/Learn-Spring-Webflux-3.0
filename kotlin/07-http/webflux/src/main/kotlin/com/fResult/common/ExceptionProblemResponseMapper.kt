package com.fResult.common

import com.fResult.http.filters.ElementNotFoundException
import com.fResult.http.filters.ProductOutOfStockException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

object ExceptionProblemResponseMapper {
  fun map(ex: Throwable): Mono<ServerResponse> {
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
}