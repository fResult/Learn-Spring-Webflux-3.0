package com.fResult.reactor.ch5_05;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TransformTest {
  @Test
  void transform() {
    final var finished = new AtomicBoolean(false);
    final var letters =
        Flux.just("A", "B", "C")
            .transform(strFlux -> strFlux.doFinally(ignored -> finished.set(true)));

    StepVerifier.create(letters).expectNextCount(3).verifyComplete();
    assertTrue(finished.get(), "The finished Boolean must be true");
  }
}
