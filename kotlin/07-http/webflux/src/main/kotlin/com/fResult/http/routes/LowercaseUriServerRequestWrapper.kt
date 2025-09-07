package com.fResult.http.routes

import org.springframework.http.server.RequestPath
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper
import java.net.URI

class LowercaseUriServerRequestWrapper(target: ServerRequest) : ServerRequestWrapper(target) {
  override fun uri(): URI = super.uri().toString().lowercase().let(URI::create)

  override fun path(): String = uri().rawPath

  override fun requestPath(): RequestPath = RequestPath.parse(path(), null)
}
