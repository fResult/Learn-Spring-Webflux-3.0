package com.fResult.reactor.ch5_07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
public class SchedulersSubscribeOnTest {
  @Test
  void subscribeOn() {
    val rsbThreadName = SchedulersSubscribeOnTest.class.getName();
    val map = new ConcurrentHashMap<String, AtomicInteger>();
    val executor = Executors.newFixedThreadPool(5, countingThreadFactory(rsbThreadName, map));
    val scheduler = Schedulers.fromExecutor(executor);
    val integerFlux =
        Mono.just(1)
            .doOnNext(log::info)
            .subscribeOn(scheduler)
            .doFinally(ignored -> map.forEach(this::logKeyValue));

    StepVerifier.create(integerFlux).expectNextCount(1).verifyComplete();
    val atomicInteger = map.getOrDefault(rsbThreadName, new AtomicInteger());
    assertEquals(1, atomicInteger.get());
  }

  private ThreadFactory countingThreadFactory(
      String rsbThreadName, ConcurrentHashMap<String, AtomicInteger> map) {
    return runnable -> {
      Runnable wrapper =
          () -> {
            val key = Thread.currentThread().getName();
            val result = map.computeIfAbsent(key, ignored -> new AtomicInteger());
            result.incrementAndGet();
            runnable.run();
          };
      return new Thread(
          runnable,
          rsbThreadName
              + "-"
              + map.computeIfAbsent(rsbThreadName, k -> new AtomicInteger()).incrementAndGet());
    };
  }

  private void logKeyValue(String key, AtomicInteger value) {
    log.info("Thread: {}, Value: {}", key, value.get());
  }
}
