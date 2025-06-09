package com.fResult.reactor.ch5_03

import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

class SimpleFluxFactoriesTest {
  @Test
  fun simple(): Unit {
    val rangeOfIntegers: Publisher<Int> = Flux.range(1, 10)
    StepVerifier.create(rangeOfIntegers).expectNextCount(10).verifyComplete()

    val letters = Flux.just('A', 'B', 'C')
    StepVerifier.create(letters).expectNext('A', 'B', 'C').verifyComplete()

    val now = System.currentTimeMillis()
    val greetingMono = Mono.just(Date(now))
    StepVerifier.create(greetingMono).expectNext(Date(now)).verifyComplete()
  }
}
