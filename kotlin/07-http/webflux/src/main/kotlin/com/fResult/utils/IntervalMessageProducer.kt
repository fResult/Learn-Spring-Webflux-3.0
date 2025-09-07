package com.fResult.utils

import reactor.core.publisher.Flux
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class IntervalMessageProducer {
  companion object {
    fun produce(count: Int): Flux<String> {
      return produce().take(count.toLong())
    }

    fun produce(): Flux<String> {
      return produceCountAndString().map(CountAndString::message)
    }

    private fun produceCountAndString(): Flux<CountAndString> {
      val counter = AtomicLong()

      return Flux.interval(1.seconds.toJavaDuration())
        .map { CountAndString(counter.incrementAndGet().toInt()) }
    }
  }
}
