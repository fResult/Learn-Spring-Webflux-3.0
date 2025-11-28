package com.fResult.orchestration

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@RestController
@RequestMapping("/profiles")
class ProfileRestController {
  private val profiles: Map<Int, Profile> = mapOf(1 to "Jane", 2 to "Mia")
    .entries.associate(::toProFileById)

  @GetMapping
  fun byId(@RequestParam(required = false, name = "customer-id") customerId: Int): Mono<Profile> = profiles[customerId].toMono()

  private fun toProFileById(entry: Map.Entry<Int, String>): Pair<Int, Profile> {
    val (id, username) = entry

    return id to Profile(id, username, UUID.randomUUID().toString())
  }
}
