package com.fResult.http.filters

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.net.URI

@Component
class LowerCaseWebFilter : WebFilter {
  override fun filter(
    webExchange: ServerWebExchange,
    chain: WebFilterChain,
  ): Mono<Void> = webExchange.request.uri.toString().lowercase()
    .let(URI::create)
    .let(exchangeWithMutatedURI(webExchange))
    .let(chain::filter)

  private fun exchangeWithMutatedURI(webExchange: ServerWebExchange): (URI) -> ServerWebExchange = { uri ->
    webExchange.mutate().request(withRewriteUriToRequest(uri)).build()
  }

  private fun withRewriteUriToRequest(uri: URI): (ServerHttpRequest.Builder) -> Unit = { builder ->
    builder.uri(uri)
  }
}
