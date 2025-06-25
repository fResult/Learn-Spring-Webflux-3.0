package com.fResult.reactor.ch5_04

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

class EmitterProcessorTest {
  @Test
  fun emitterProcessor() {
    val sinkEmitter = Sinks.many().multicast().onBackpressureBuffer<String>()
    produce(sinkEmitter)
    consume(sinkEmitter.asFlux())
  }

  private fun produce(sink: Sinks.Many<String>) {
    sink.tryEmitNext("1")
    sink.tryEmitNext("2")
    sink.tryEmitNext("3")
    sink.tryEmitComplete()
  }

  private fun consume(publisher: Flux<String>) {
    StepVerifier.create(publisher)
      .expectNext("1")
      .expectNext("2")
      .expectNext("3")
      .verifyComplete()
  }
}
