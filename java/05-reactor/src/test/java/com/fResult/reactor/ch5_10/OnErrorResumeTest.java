package com.fResult.reactor.ch5_10;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class OnErrorResumeTest {
  private final Flux<Integer> resultsInError = Flux.just(1, 2, 3).flatMap(this::failOn2);

  @Test
  void onErrorResume() {
    val integers =
        resultsInError.onErrorResume(IllegalArgumentException.class, ignored -> Flux.just(3, 2, 1));

    StepVerifier.create(integers).expectNext(1, 3, 2, 1).verifyComplete();
  }

  private Flux<Integer> failOn2(int counter) {
    return counter == 2 ? Flux.error(new IllegalArgumentException("Oops!")) : Flux.just(counter);
  }
}
