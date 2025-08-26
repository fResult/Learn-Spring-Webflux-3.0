package com.fResult.reactor.ch5_10;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
class OnErrorReturnTest {
  private final Flux<Integer> resultsInError = Flux.just(1, 2, 3).flatMap(this::failOn2);

  @Test
  void onErrorReturn() {
    val integers = resultsInError.onErrorReturn(0);
    StepVerifier.create(integers).expectNext(1, 0).verifyComplete();
  }

  private Flux<Integer> failOn2(int counter) {
    return counter == 2 ? Flux.error(new IllegalArgumentException("Oops!")) : Flux.just(counter);
  }
}
