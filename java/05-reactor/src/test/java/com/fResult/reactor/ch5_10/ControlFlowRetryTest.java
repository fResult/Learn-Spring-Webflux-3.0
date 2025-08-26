package com.fResult.reactor.ch5_10;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

@Log4j2
class ControlFlowRetryTest {
  private static final String MESSAGE = "Hello, world!";

  @Test
  void retry() {
    val errored = new AtomicBoolean(false);
    val producer = Flux.create(simulatedErrorThenEmitHello(errored));

    val retryOnError =
        producer
            .doOnError(error -> log.error("Error occurred: {}", error.getMessage()))
            .retry(1)
            .doOnNext(value -> log.info("Received value: {}", value))
            .doOnComplete(() -> log.info("Stream completed successfully"));

    StepVerifier.create(retryOnError).expectNext(MESSAGE).verifyComplete();
  }

  private Consumer<? super FluxSink<String>> simulatedErrorThenEmitHello(AtomicBoolean errored) {
    return stringSink -> {
      val isFirstTime = errored.compareAndSet(false, true);

      if (isFirstTime) {
        log.error("Emitting error: {}", RuntimeException.class.getName());
        stringSink.error(new RuntimeException("Simulated error"));
      } else {
        log.info("Emitting: {}", MESSAGE);
        stringSink.next(MESSAGE);
        stringSink.complete();
      }
    };
  }
}
