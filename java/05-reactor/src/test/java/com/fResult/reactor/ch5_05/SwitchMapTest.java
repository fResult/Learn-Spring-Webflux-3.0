package com.fResult.reactor.ch5_05;

import java.time.Duration;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class SwitchMapTest {
  @Test
  void switchMapWithLookahead() {
    val source =
        Flux.just("re", "rea", "reac", "react", "reacti", "reactiv", "reactive")
            .delayElements(Duration.ofMillis(100))
            .switchMap(this::lookup);

    StepVerifier.create(source)
        .expectNext("reactive -> reactive")
        .expectComplete()
        .verify(Duration.ofSeconds(5));
  }

  private Flux<String> lookup(String word) {
    return Flux.just(word + " -> reactive").delayElements(Duration.ofMillis(500));
  }
}
