package com.fResult.reactor.ch5_05;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TakeTest {
  @Test
  void take() {
    val count = 10;
    val take = range().take(count);

    StepVerifier.create(take).expectNextCount(count).verifyComplete();
  }

  @Test
  void takeUntil() {
    val count = 50;
    val take = range().takeUntil(i -> i == (count - 1));

    StepVerifier.create(take).expectNextCount(count).verifyComplete();
  }

  private Flux<Integer> range() {
    return Flux.range(0, 1000);
  }
}
