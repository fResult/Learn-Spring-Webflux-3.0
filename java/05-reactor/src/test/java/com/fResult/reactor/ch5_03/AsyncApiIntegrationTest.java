package com.fResult.reactor.ch5_03;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

@Log4j2
class AsyncApiIntegrationTest {
  private final ExecutorService executorService = Executors.newFixedThreadPool(1);

  @Test
  void async() {
    val integers = Flux.create(launch(5));
    StepVerifier.create(integers.doFinally(ignored -> executorService.shutdown()))
        .expectNextCount(5)
        .verifyComplete();
  }

  private Consumer<FluxSink<Integer>> launch(int count) {
    return integerFluxSink -> {
      val integer = new AtomicInteger();
      assertNotNull(integerFluxSink);
      while (integer.get() < count) {
        val random = (long) (Math.random() * 1000);
        integerFluxSink.next(integer.incrementAndGet());
        sleep(random);
      }
      integerFluxSink.complete();
    };
  }

  private void sleep(long ms) {
    log.info("Sleeping for {} ms", ms);
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {
      log.error(ex);
    }
  }
}
