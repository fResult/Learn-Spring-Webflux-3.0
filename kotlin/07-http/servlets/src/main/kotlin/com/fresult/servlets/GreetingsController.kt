package com.fresult.servlets

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class GreetingsController {
  @GetMapping("/hello/controller/{name}")
  fun greet(request: ServerHttpRequest, @PathVariable name: String): Mono<Greetings> = Greetings.greet("controller", name)
}
