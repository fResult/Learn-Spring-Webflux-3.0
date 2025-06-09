package com.fResult.reactor.ch5_03

import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
class SimpleFluxFactoriesTest {
  @Test
  fun simple(): Unit {
    val rangeOfIntegers: Publisher<Int> = Flux.range(1, 10)
    StepVerifier.create(rangeOfIntegers).expectNextCount(10).verifyComplete()
  }
}
