package com.fResult.reactor.ch5_05

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class FilterTest {
  @Test
  fun filter() {
    val range = Flux.range(0, 1000).take(5)
    val filtered = range.filter { it % 2 == 0 }

    StepVerifier.create(filtered).expectNext(0, 2, 4).verifyComplete()
  }
}
