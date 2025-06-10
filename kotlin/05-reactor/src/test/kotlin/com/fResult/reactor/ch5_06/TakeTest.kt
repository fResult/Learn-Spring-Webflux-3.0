package com.fResult.reactor.ch5_06

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class TakeTest {
  @Test
  fun take() {
    val count = 10L
    val takenNumbers = range().take(count)
    StepVerifier.create(takenNumbers).expectNextCount(count).verifyComplete()
  }

  @Test
  fun takeUntil() {
    val count = 50L
    val takenNumbers = range().takeUntil { it.toLong() == count - 1 }
    StepVerifier.create(takenNumbers)
      .expectNextCount(count)
      .verifyComplete()
  }

  private fun range() = Flux.range(0, 1000)
}
