package com.fResult.reactor.ch5_09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;

@Log4j2
class ContextTest {
  @Test
  void context() throws InterruptedException {
    val observedContextValues = new ConcurrentHashMap<String, AtomicInteger>();
    val max = 3;
    val countDownLatch = new CountDownLatch(max);
    val key = "key1";
    val expectedContext = Context.of(key, "value1");
    val contextAwareFlux =
        Flux.range(0, max)
            .delayElements(Duration.ofMillis(1))
            .doOnEach(assertAndCountContextOnNextSignal(observedContextValues, key))
            .contextWrite(expectedContext);

    contextAwareFlux.subscribe(
        logAndCountDown(countDownLatch),
        ex -> log.error("Error occurred: {}", ex.getMessage()),
        () -> log.info("Completed successfully"));

    countDownLatch.await();

    val contextAtomicInt = observedContextValues.getOrDefault(key, new AtomicInteger());
    assertEquals(max, contextAtomicInt.get());
  }

  private Consumer<? super Integer> logAndCountDown(CountDownLatch latch) {
    return value -> {
      try {
        log.info("Received value: {}", value);
        latch.countDown();
        log.info("Count down, remaining: {}", latch.getCount());
      } catch (Exception ex) {
        throw new RuntimeException("Error in count down", ex);
      }
    };
  }

  private Consumer<? super Signal<Integer>> assertAndCountContextOnNextSignal(
      ConcurrentHashMap<String, AtomicInteger> contextObservationMap, String key) {

    return intSignal -> {
      val currentContext = intSignal.getContextView();
      if (intSignal.getType() == SignalType.ON_NEXT) {
        val key1 = currentContext.<String>get(key);

        assertNotNull(key1);
        assertEquals("value1", key1, "Context value should match");
        contextObservationMap.computeIfAbsent(key, k -> new AtomicInteger()).incrementAndGet();
      }
    };
  }
}
