package com.fResult.reactor.ch5_05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ThenManyTest {
  @Test
  void thenMany() {
    val letters = new AtomicInteger();
    val numbers = new AtomicInteger();
    val lettersPublisher =
        Flux.just("a", "b", "c").doOnNext(character -> letters.incrementAndGet());
    val numbersPublisher = Flux.just(1, 2, 3).doOnNext(num -> numbers.incrementAndGet());
    val thisBeforeThat = lettersPublisher.thenMany(numbersPublisher);

    StepVerifier.create(thisBeforeThat).expectNext(1, 2, 3).verifyComplete();
    assertEquals(3, letters.get());
    assertEquals(3, numbers.get());
  }
}
