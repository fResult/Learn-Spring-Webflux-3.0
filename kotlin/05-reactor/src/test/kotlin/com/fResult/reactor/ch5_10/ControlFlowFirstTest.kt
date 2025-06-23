package com.fResult.reactor.ch5_10

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class ControlFlowFirstTest {
  @Test
  fun first() {
    val slow = Flux.just(1, 2, 3).delayElements(10.milliseconds.toJavaDuration())
    val fast = Flux.just(4, 5, 6, 7).delayElements(2.milliseconds.toJavaDuration())
    val firstEmittedStream = Flux.firstWithSignal(slow, fast)

    StepVerifier.create(firstEmittedStream).expectNext(4, 5, 6, 7).verifyComplete()
  }
}
