package com.fResult.reactor.ch5_05

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class ThenManyTest {
  @Test
  fun thenMany() {
    val letters = AtomicInteger()
    val numbers = AtomicInteger()

    val lettersPublisher = Flux.just("a", "b", "c").doOnNext { letters.incrementAndGet() }
    val numbersPublisher = Flux.just(1, 2, 3).doOnNext { numbers.incrementAndGet() }

    val thisBeforeThat = lettersPublisher.thenMany(numbersPublisher)

    StepVerifier.create(thisBeforeThat).expectNext(1, 2, 3).verifyComplete()
    assertEquals(letters.get(), 3)
    assertEquals(numbers.get(), 3)
  }

  @Test
  fun then() {
    val letters = AtomicInteger()
    val numbers = AtomicInteger()

    val lettersPublisher = Flux.just("a", "b", "c").doOnNext { letters.incrementAndGet() }
    val numberPublisher = Mono.just(1).doOnNext { numbers.incrementAndGet() }

    val thisThenThat = lettersPublisher.then(numberPublisher)

    StepVerifier.create(thisThenThat).expectNext(1).verifyComplete()
    assertEquals(letters.get(), 3)
    assertEquals(numbers.get(), 1)
  }
}

