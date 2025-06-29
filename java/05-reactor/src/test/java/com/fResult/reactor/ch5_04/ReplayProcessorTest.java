package com.fResult.reactor.ch5_04;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

class ReplayProcessorTest {
  @Test
  void replayProcessor() {
    val historySize = 2;
    val processor = Sinks.many().replay().<String>limit(historySize);
    produce(processor);
    consume(processor.asFlux());
  }

  private void produce(Sinks.Many<String> sink) {
    for (var i = 0; i < 3; i++) sink.tryEmitNext(String.valueOf(i + 1));
    sink.tryEmitComplete();
  }

  private void consume(Flux<String> publisher) {
    for (var i = 0; i < 5; i++)
      StepVerifier.create(publisher).expectNext("2").expectNext("3").verifyComplete();
  }
}
