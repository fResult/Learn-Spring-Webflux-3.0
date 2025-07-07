package com.fResult.reactor.ch5_05;

import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
class HandleTest {
  @Test
  void handle() {
    StepVerifier.create(emitUntilErrorValue(5, 4))
        .expectNext(0, 1, 2, 3)
        .expectError(IllegalArgumentException.class)
        .verify();

    StepVerifier.create(emitUntilErrorValue(3, 3)).expectNext(0, 1, 2).verifyComplete();
  }

  private Flux<Integer> emitUntilErrorValue(int max, int errorAt) {
    return Flux.range(0, max)
        .handle(
            (value, sink) -> {
              val upTo = Stream.iterate(0, i -> i < errorAt, i -> i + 1).toList();

              if (upTo.contains(value)) {
                sink.next(value);
                return;
              }
              if (value == errorAt) {
                sink.error(new IllegalArgumentException("No " + errorAt + " for you!"));
                return;
              }

              sink.complete();
            });
  }
}
