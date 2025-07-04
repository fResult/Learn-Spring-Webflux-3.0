package com.fResult.reactor.ch5_05;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
class FilterTest {
  @Test
  void filter() {
    val range = Flux.range(0, 1000).take(5);
    val filtered = range.filter(i -> i % 2 == 0);

    StepVerifier.create(filtered).expectNext(0, 2, 4).verifyComplete();
  }
}
