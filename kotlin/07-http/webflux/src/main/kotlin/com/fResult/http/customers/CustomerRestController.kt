package com.fResult.http.customers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.net.URI

@RestController
@RequestMapping("/rc/customers")
class CustomerRestController(private val repository: CustomerRepository) {
  @GetMapping
  fun all() = repository.findAll()

  @GetMapping("/{id}")
  fun byId(@PathVariable id: String) = repository.findById(id)
    .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))

  @PostMapping
  fun create(@RequestBody customer: Customer): Mono<ResponseEntity<Customer>> = repository.save(customer)
    .map { ResponseEntity.created(URI.create("/rc/customers/${it.id}")).body(it) }
}
