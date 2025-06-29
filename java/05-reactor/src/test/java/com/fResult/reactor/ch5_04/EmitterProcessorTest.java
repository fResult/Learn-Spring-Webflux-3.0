package com.fResult.reactor.ch5_04;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

class EmitterProcessorTest {
  @Test
  void emitterProcessor() {
    val processor = Sinks.many().multicast().<String>onBackpressureBuffer();
    produce(processor);
    consume(processor.asFlux());
  }

  private void produce(Sinks.Many<String> sink) {
    for (var i = 0; i < 3; i++) sink.tryEmitNext(String.valueOf(i + 1));
    sink.tryEmitComplete();
  }

  private void consume(Flux<String> publisher) {
    StepVerifier.create(publisher).expectNext("1").expectNext("2").expectNext("3").verifyComplete();
  }
}
