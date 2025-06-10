package com.fResult.reactor.ch5_04

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

class ReplayProcessorTest {
  @Test
  fun replayProcessor() {
    val historySize = 2
    val sinkReplayer = Sinks.many().replay().limit<String>(historySize)
    produce(sinkReplayer)
    consume(sinkReplayer.asFlux())
  }

  private fun produce(sink: Sinks.Many<String>) {
    sink.tryEmitNext("1")
    sink.tryEmitNext("2")
    sink.tryEmitNext("3")
    sink.tryEmitComplete()
  }

  private fun consume(publisher: Flux<String>) {
    for (i in 0 until 5) {
      StepVerifier.create(publisher).expectNext("2").expectNext("3").verifyComplete()
    }
  }
}
