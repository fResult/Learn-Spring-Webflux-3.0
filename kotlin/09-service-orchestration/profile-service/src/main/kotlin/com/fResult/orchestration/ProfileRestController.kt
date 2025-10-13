package com.fResult.orchestration

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@RestController
@RequestMapping("/profiles")
class ProfileRestController {
  private val profiles: Map<Int, Profile> = mapOf(1 to "Jane", 2 to "Mia")
    .entries.associate(::toProFileById)

  @GetMapping("/{id}")
  fun byId(@PathVariable id: Int): Mono<Profile> = profiles[id].toMono()

  private fun toProFileById(entry: Map.Entry<Int, String>): Pair<Int, Profile> {
    val (id, username) = entry

    return id to Profile(id, username, UUID.randomUUID().toString())
  }
}
