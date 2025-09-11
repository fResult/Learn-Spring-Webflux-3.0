package com.fresult.service

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Instant

@RestController
class GreetingController {
  @GetMapping("/greetings")
  fun greet(@AuthenticationPrincipal user: Mono<UserDetails>): Mono<Map<String, String>> {
    return user.map { user ->
      @Suppress("NAME_SHADOWING")
      mapOf(
        "message" to "Hello, ${user.username} @ ${Instant.now()}!",
        "roles" to user.authorities.joinToString(", ") { it.authority }
      )
    }
  }
}
