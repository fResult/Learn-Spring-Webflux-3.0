package com.fResult.reactor.ch5_08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.function.Consumer;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;

class HotStream1Test {
  @Test
  void hot() {
    val first = new ArrayList<Integer>();
    val second = new ArrayList<Integer>();

    val sink = Sinks.many().multicast().<Integer>onBackpressureBuffer(2);
    val flux = sink.asFlux();

    flux.doOnNext(logInfo("First")).subscribe(first::add);
    sink.tryEmitNext(1);
    sink.tryEmitNext(2);

    flux.doOnNext(logInfo("Second")).subscribe(second::add);
    sink.tryEmitNext(3);
    sink.tryEmitComplete();

    assertEquals(3, first.size());
    assertEquals(1, second.size());
  }

  private Consumer<Integer> logInfo(String name) {
    return num -> System.out.println("[" + name + "]: " + num);
  }
}
