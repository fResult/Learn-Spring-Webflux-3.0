package com.fResult.reactor.ch5_10;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.Duration;
import java.util.concurrent.TimeoutException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Log4j2
class ControlFlowTimeoutTest {
  @Test
  void timeout() {
    val ids =
        Flux.just(1, 2, 3)
            .delayElements(Duration.ofSeconds(1))
            .timeout(Duration.ofMillis(500))
            .onErrorResume(this::fallbackOnTimeout);

    StepVerifier.create(ids).expectNext(0).verifyComplete();
  }

  private Publisher<? extends Integer> fallbackOnTimeout(Throwable ex) {
    log.warn("Timeout occurred: {}", ex.getMessage());
    assertInstanceOf(
        TimeoutException.class,
        ex,
        "Expected TimeoutException, but got: " + ex.getClass().getSimpleName());
    return Flux.just(0);
  }
}
