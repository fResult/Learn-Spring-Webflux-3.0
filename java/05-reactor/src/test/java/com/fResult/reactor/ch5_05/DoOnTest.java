package com.fResult.reactor.ch5_05;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

@Log4j2
class DoOnTest {
  @Test
  void doOn() {
    val signals = new ArrayList<Signal<Integer>>();
    val nextValues = new ArrayList<Integer>();
    val subscriptions = new ArrayList<Subscription>();
    val exceptions = new ArrayList<Throwable>();
    val finallySignalTypes = new ArrayList<SignalType>();

    val on =
        Flux.<Integer>create(
                intFluxSink -> {
                  intFluxSink.next(1);
                  intFluxSink.next(2);
                  intFluxSink.next(3);
                  intFluxSink.error(new IllegalArgumentException("Oops"));
                  intFluxSink.complete();
                })
            .doOnNext(nextValues::add)
            .doOnEach(signals::add)
            .doOnSubscribe(subscriptions::add)
            .doOnError(IllegalArgumentException.class, exceptions::add)
            .doFinally(finallySignalTypes::add);

    StepVerifier.create(on)
        .expectNext(1, 2, 3)
        .expectError(IllegalArgumentException.class)
        .verify();

    signals.forEach(log::info);
    assertEquals(4, signals.size());

    finallySignalTypes.forEach(log::info);
    assertEquals(1, finallySignalTypes.size());

    subscriptions.forEach(log::info);
    assertEquals(1, subscriptions.size());

    exceptions.forEach(log::info);
    assertEquals(1, exceptions.size());
    assertInstanceOf(IllegalArgumentException.class, exceptions.getFirst());

    nextValues.forEach(log::info);
    assertEquals(List.of(1, 2, 3), nextValues);
  }
}
