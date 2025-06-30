package com.fResult.reactor.ch5_10

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class ControlFlowMergeTest {
  @Test
  fun merge() {
    val fastest = Flux.just(5, 6)
    val secondFastest = Flux.just(1, 2).delayElements(2.milliseconds.toJavaDuration())
    val slowest = Flux.just(3, 4).delayElements(20.milliseconds.toJavaDuration())

    val streamOfStreams = Flux.just(slowest, secondFastest, fastest)
    val merged = Flux.merge(streamOfStreams)

    StepVerifier.create(merged)
      .expectNext(5, 6, 1, 2, 3, 4)
      .expectComplete()
      .verify()
  }
}
