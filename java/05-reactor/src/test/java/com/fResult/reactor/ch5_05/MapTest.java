package com.fResult.reactor.ch5_05;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MapTest {
  @Test
  void maps() {
    val characters = Flux.just("a", "b", "c").map(String::toUpperCase);

    StepVerifier.create(characters).expectNext("A", "B", "C").verifyComplete();
  }
}
