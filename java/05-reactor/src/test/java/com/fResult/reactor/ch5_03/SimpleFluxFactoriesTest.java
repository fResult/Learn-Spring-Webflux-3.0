package com.fResult.reactor.ch5_03;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SimpleFluxFactoriesTest {
  @Test
  void simple() {
    Publisher<Integer> rangeOfIntegers = Flux.range(0, 10);
    StepVerifier.create(rangeOfIntegers).expectNextCount(10).verifyComplete();

    final var letters = Flux.just("A", "B", "C");
    StepVerifier.create(letters).expectNext("A", "B", "C").verifyComplete();

    final var now = System.currentTimeMillis();
    final var greetingMono = Mono.just(new Date(now));
    StepVerifier.create(greetingMono).expectNext(new Date(now)).verifyComplete();

    final var emptyFlux = Flux.empty();
    StepVerifier.create(emptyFlux).verifyComplete();

    final var fromArray = Flux.fromArray(new Integer[] {1, 2, 3});
    StepVerifier.create(fromArray).expectNext(1, 2, 3).verifyComplete();

    final var fromIterable = Flux.fromIterable(List.of(1, 2, 3));
    StepVerifier.create(fromIterable).expectNext(1, 2, 3).verifyComplete();

    final var integer = new AtomicInteger();
    Supplier<Integer> incrementAndGet = integer::incrementAndGet;
    final var integerFlux = Flux.fromStream(Stream.generate(incrementAndGet));
    StepVerifier.create(integerFlux.take(3))
        .expectNext(1)
        .expectNext(2)
        .expectNext(3)
        .verifyComplete();
  }
}
