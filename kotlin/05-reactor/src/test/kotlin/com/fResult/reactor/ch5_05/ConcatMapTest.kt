package com.fResult.reactor.ch5_05

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class ConcatMapTest {
  @Test
  fun concatMap() {
    val numbers = Flux.just(Pair(1, 300L), Pair(2, 200L), Pair(3, 100L))
      .concatMap { id -> delayReplyFor(id.first, id.second) }

    StepVerifier.create(numbers).expectNext(1, 2, 3).verifyComplete()
  }
}

private fun delayReplyFor(n: Int, delay: Long) =
  Flux.just(n).delayElements(delay.milliseconds.toJavaDuration())
