package com.fResult.reactor.ch5_10;

import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
class ControlFlowFirstTest {
  @Test
  void first() {
    val slow = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(10));
    val fast = Flux.just(4, 5, 6, 7).delayElements(Duration.ofMillis(1));
    val first = Flux.firstWithSignal(slow, fast);

    StepVerifier.create(first).expectNext(4, 5, 6, 7).verifyComplete();
  }
}
