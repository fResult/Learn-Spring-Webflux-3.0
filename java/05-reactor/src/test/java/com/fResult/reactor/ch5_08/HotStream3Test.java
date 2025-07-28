package com.fResult.reactor.ch5_08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Log4j2
class HotStream3Test {
  private final List<Integer> first = new ArrayList<Integer>();
  private final List<Integer> second = new ArrayList<Integer>();
  private final List<Integer> third = new ArrayList<Integer>();

  @Test
  void hot() {
    val minSubscribers = 3;
    val pileOn =
        Flux.just(1, 2, 3)
            .publish()
            .autoConnect(minSubscribers)
            .subscribeOn(Schedulers.immediate());
    pileOn.subscribe(first::add);
    assertEquals(0, first.size());

    pileOn.subscribe(second::add);
    assertEquals(0, second.size());

    pileOn.subscribe(third::add);
    assertEquals(3, third.size());
    assertEquals(3, first.size());
    assertEquals(3, second.size());
  }
}
