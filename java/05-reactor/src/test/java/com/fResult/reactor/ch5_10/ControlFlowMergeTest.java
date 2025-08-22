package com.fResult.reactor.ch5_10;

import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

class ControlFlowMergeTest {
  @Test
  void merge() {
    val fastest = Flux.just(5, 6);
    val secondFastest = Flux.just(1, 2).delayElements(Duration.ofMillis(2));
    val slowest = Flux.just(3, 4).delayElements(Duration.ofMillis(20));

    val streamOfStreams = Flux.just(slowest, secondFastest, fastest);
    val merged = Flux.merge(streamOfStreams);

    StepVerifier.create(merged)
        .expectNext(5, 6, 1, 2, 3, 4)
        .expectComplete()
        .verify();
  }
}
