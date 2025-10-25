package com.fResult.orchestration.hedging

import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.Loggers
import java.net.URI
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class HedgingFilterFunction(
  private val reactiveDiscoveryClient: ReactiveDiscoveryClient,
  private val maxNodes: Int,
) : ExchangeFilterFunction {
  private val timeoutInSeconds = 10

  companion object {
    private val log = Loggers.getLogger(HedgingFilterFunction::class.java)
  }

  override fun filter(
    request: ClientRequest,
    nextExchange: ExchangeFunction
  ): Mono<ClientResponse?> {
    val requestUri = request.url()
    val apiName = requestUri.host

    return reactiveDiscoveryClient.getInstances(apiName)
      .collectList()
      .map(::toShuffled)
      .flatMapMany(Flux<ServiceInstance>::fromIterable)
      .take(maxNodes.toLong())
      .map(toResolvedUri(requestUri))
      .map(invoke(request, nextExchange))
      .collectList()
      .flatMap(::selectWinningResponse)
  }

  private fun toResolvedUri(origRequestUri: URI): (ServiceInstance) -> URI = { server ->
    URI.create("${origRequestUri.scheme}://${server.host}:${server.port}${origRequestUri.path}")
  }

  private fun invoke(
    request: ClientRequest,
    nextExchange: ExchangeFunction,
  ): (URI) -> Mono<ClientResponse> = { uri ->
    val newRequest = ClientRequest.create(request.method(), uri)
      .headers { headers -> headers.addAll(request.headers()) }
      .cookies { cookies -> cookies.addAll(request.cookies()) }
      .attributes { attrs -> attrs.putAll(request.attributes()) }
      .body(request.body())
      .build()

    nextExchange.exchange(newRequest).doOnNext { log.info("Launching ${request.url()}") }
  }

  private fun selectWinningResponse(clientResponses: List<Mono<ClientResponse>>): Mono<ClientResponse> {
    return Flux.firstWithSignal(clientResponses).timeout(timeoutInSeconds.seconds.toJavaDuration()).singleOrEmpty()
  }

  private fun <T> toShuffled(list: List<T>) =
    list.toMutableList().apply { shuffle() }.toList()
}
