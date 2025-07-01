package com.fResult.reactor.ch5_05;

import java.time.Duration;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ConcatMapTest {
  @Test
  void concatMap() {
    val data =
        Flux.just(new Pair(1, 300), new Pair(2, 200), new Pair(3, 100))
            .concatMap(id -> delayReplyFor(id.id(), id.delay()));

    StepVerifier.create(data).expectNext(1, 2, 3).verifyComplete();
  }

  private Flux<Integer> delayReplyFor(int i, long delay) {
    return Flux.just(i).delayElements(Duration.ofMillis(delay));
  }

  static record Pair(int id, long delay) {}
}
