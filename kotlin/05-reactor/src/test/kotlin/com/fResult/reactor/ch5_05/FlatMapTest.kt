package com.fResult.reactor.ch5_05

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration


class FlatMapTest {
  private val log = LogManager.getLogger(FlatMapTest::class.java)

  @Test
  fun flatMap() {
    val numbers = Flux.just(Pair(1, 300L), Pair(2, 200L), Pair(3, 100L))
      .flatMap { id -> delayReplyFor(id.first, id.second) }

    StepVerifier.create(numbers).expectNext(3, 2, 1).verifyComplete()
  }
}

private fun delayReplyFor(n: Int, delay: Long) =
  Flux.just(n).delayElements(delay.milliseconds.toJavaDuration())
