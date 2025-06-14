package com.fResult.reactor.ch5_05

import org.apache.logging.log4j.LogManager
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import reactor.core.publisher.Signal
import reactor.core.publisher.SignalType
import reactor.test.StepVerifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DoOnTest {
  private val log = LogManager.getLogger(DoOnTest::class)

  @Test
  fun doOn() {
    val signals = arrayListOf<Signal<Int>>()
    val nextValues = mutableListOf<Int>()
    val subscriptions = arrayListOf<Subscription>()
    val exceptions = arrayListOf<Throwable>()
    val finallySignals = arrayListOf<SignalType>()

    val on = Flux.create { sink ->
      sink.apply {
        next(1)
        next(2)
        next(3)
        error(IllegalArgumentException("Oops!"))
        complete()
      }
    }
      .doOnNext(nextValues::add)
      .doOnEach(signals::add)
      .doOnSubscribe(subscriptions::add)
      .doOnError(IllegalArgumentException::class.java, exceptions::add)
      .doFinally(finallySignals::add)

    StepVerifier.create(on).expectNext(1, 2, 3)
      .expectError(IllegalArgumentException::class.java)
      .verify()

    signals.forEach(log::info)
    assertEquals(4, signals.size)

    finallySignals.forEach(log::info)
    assertEquals(finallySignals.size, 1)

    subscriptions.forEach(log::info)
    assertEquals(subscriptions.size, 1)

    exceptions.forEach(log::error)
    assertEquals(exceptions.size, 1)
    assertTrue(exceptions.firstOrNull() is IllegalArgumentException)

    nextValues.forEach(log::info)
    assertEquals(listOf(1, 2, 3), nextValues)
  }
}
