package com.fResult.reactor.ch5_05

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class MapTest {
  @Test
  fun maps() {
    val upperStrings = Flux.just("a", "b", "c", "d", "e").map(String::uppercase)
    StepVerifier.create(upperStrings).expectNext("A", "B", "C", "D", "E").verifyComplete()
  }
}
