package com.fResult.orchestration

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestControllerAdvice
class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgument(ex: IllegalArgumentException): Mono<ResponseEntity<ProblemDetail>> =
    ProblemDetail.forStatusAndDetail(
      HttpStatus.BAD_REQUEST,
      "${IllegalArgumentException::class.simpleName}: ${ex.message ?: "Invalid argument"}",
    )
      .toMono()
      .map(ResponseEntity.internalServerError()::body)
}
