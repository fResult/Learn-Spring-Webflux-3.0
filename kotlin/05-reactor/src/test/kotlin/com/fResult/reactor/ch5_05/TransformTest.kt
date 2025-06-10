package com.fResult.reactor.ch5_05

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.Test
import kotlin.test.assertTrue

class TransformTest {
  @Test
  fun transform() {
    val finished = AtomicBoolean()
    val letters =
      Flux.just("A", "B", "C", "D", "E").transform { stringFlux -> stringFlux.doFinally { finished.set(true) } }

    StepVerifier.create(letters).expectNextCount(5).verifyComplete()
    assertTrue(finished.get(), "The `finished` flag must be true")
  }
}
