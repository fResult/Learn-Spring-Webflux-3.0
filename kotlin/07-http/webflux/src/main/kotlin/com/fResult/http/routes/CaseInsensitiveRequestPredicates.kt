package com.fResult.http.routes

import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.ServerRequest

class CaseInsensitiveRequestPredicates(private val target: RequestPredicate) : RequestPredicate {
  companion object {
    fun i(predicate: RequestPredicate) = CaseInsensitiveRequestPredicates(predicate)
  }

  override fun test(request: ServerRequest): Boolean =
    target.test(LowercaseUriServerRequestWrapper(request))

  override fun toString(): String = target.toString()
}
