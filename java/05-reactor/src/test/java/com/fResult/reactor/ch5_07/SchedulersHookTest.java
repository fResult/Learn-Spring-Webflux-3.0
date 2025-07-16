package com.fResult.reactor.ch5_07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
public class SchedulersHookTest {
  private static final String HOOK_NAME = "my-hook";

  @Test
  void onScheduleHook() {
    val counter = new AtomicInteger();
    Schedulers.onScheduleHook(HOOK_NAME, wrapRunnableWithCounting(counter));
    val integers =
        Flux.just(1, 2, 3).delayElements(Duration.ofMillis(1)).subscribeOn(Schedulers.immediate());

    StepVerifier.create(integers).expectNext(1, 2, 3).verifyComplete();
    assertEquals(3, counter.get(), "Counter should be 3 as we have 3 elements");
  }

  private UnaryOperator<Runnable> wrapRunnableWithCounting(AtomicInteger counter) {
    return runnable ->
        () -> {
          val threadName = Thread.currentThread().getName();
          counter.incrementAndGet();
          log.info("Before execution on thread: {}", threadName);
          runnable.run();
          log.info("After execution on thread: {}", threadName);
        };
  }
}
