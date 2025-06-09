package com.fResult.reactor.ch5_03

import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream

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

    val empty: Mono<Any> = Mono.empty()
    StepVerifier.create(empty).verifyComplete()

    val fromIterable = Flux.fromIterable(listOf(1, 2, 3))
    StepVerifier.create(fromIterable).expectNext(1, 2, 3).verifyComplete()

    val fromArray = Flux.fromArray(arrayOf("A", "B", "C"))
    StepVerifier.create(fromArray).expectNext("A", "B", "C").verifyComplete()

    val integer = AtomicInteger()
    val incrementAndGet = integer::incrementAndGet
    val integers = Flux.fromStream(Stream.generate(incrementAndGet))
    StepVerifier.create(integers.take(3))
      .expectNext(1)
      .expectNext(2)
      .expectNext(3)
      .verifyComplete()
  }
}
