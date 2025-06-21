package com.fResult.reactor.ch5_10

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class ControlFlowZipTest {
  @Test
  fun zip() {
    val first = Flux.just(1, 2, 3)
    val second = Flux.just("a", "b", "c")
    val zipped = Flux.zip(first, second) { a, b -> "$a:$b" }

    StepVerifier.create(zipped)
      .expectNext("1:a", "2:b", "3:c")
      .verifyComplete()
  }
}
