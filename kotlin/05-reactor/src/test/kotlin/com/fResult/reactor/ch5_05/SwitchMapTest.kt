package com.fResult.reactor.ch5_05

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class SwitchMapTest {
  @Test
  fun switchMapWithLookaheads() {
    val source = Flux.just("re", "rea", "react", "reactive")
      .delayElements(100L.milliseconds.toJavaDuration())
      .switchMap(::lookup)

    StepVerifier.create(source)
      .expectNext("reactive -> reactive")
      .verifyComplete()
  }

  private fun lookup(word: String): Flux<String> {
    return Flux.just("$word -> reactive").delayElements(500L.milliseconds.toJavaDuration())
  }
}
