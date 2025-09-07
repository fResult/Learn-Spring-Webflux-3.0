package com.fResult.http.customers

import com.fResult.utils.respondNotFound
import com.fResult.utils.respondWithOkStreamBody
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.net.URI

@Component
class CustomerHandler(private val repository: CustomerRepository) {
  fun handleFindAll(ignored: ServerRequest): Mono<ServerResponse> =
    repository.findAll()
      .collectList()
      .flatMap(::listToOkResponse)

  fun handleFindById(request: ServerRequest): Mono<ServerResponse> =
    request.pathVariable("id")
      .let(repository::findById)
      .flatMap(ServerResponse.ok()::bodyValue)
      .switchIfEmpty(::respondNotFound)

  fun handleCreate(request: ServerRequest): Mono<ServerResponse> =
    request.bodyToMono(Customer::class.java)
      .flatMap(repository::save)
      .flatMap(respondWithCreatedResponse("/fe/customers"))
}

private inline fun <reified T> listToOkResponse(responseBody: List<T>): Mono<ServerResponse> =
  responseBody.toMono()
    .flatMapMany(Flux<T>::fromIterable)
    .let(::respondWithOkStreamBody)

private inline fun <reified T : EntityObject> respondWithCreatedResponse(
  pathPrefix: String,
): (T) -> Mono<ServerResponse> = {
  ServerResponse.created(URI.create("$pathPrefix/${it.id}")).bodyValue(it)
}
