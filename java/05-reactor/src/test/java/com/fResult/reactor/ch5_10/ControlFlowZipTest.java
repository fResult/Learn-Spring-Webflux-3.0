package com.fResult.reactor.ch5_10;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ControlFlowZipTest {
  @Test
  void zip() {
    val first = Flux.just(1, 2, 3);
    val second = Flux.just("a", "b", "c");
    val zipped = Flux.zip(first, second, (a, b) -> a + ":" + b);

    StepVerifier.create(zipped).expectNext("1:a", "2:b", "3:c").expectComplete().verify();
  }
}
