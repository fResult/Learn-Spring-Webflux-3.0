package com.fresult.servlets

import reactor.core.publisher.Mono

data class Greetings(val message: String) {
  companion object {
    fun greet(contextPath: String, name: String): Mono<Greetings> = Mono.just(Greetings("Hello, $name from $contextPath!"))
  }
}
