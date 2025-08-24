package com.fResult.reactor.ch5_10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class OnErrorMapTest {
  @Test
  void onErrorMap() {
    val counter = new AtomicInteger();
    val resultsInError = Flux.error(new IllegalArgumentException("Oops!"));
    val errorHandlingStream =
        resultsInError
            .onErrorMap(IllegalArgumentException.class, GenericException::new)
            .doOnError(GenericException.class, ignored -> counter.incrementAndGet());

    StepVerifier.create(errorHandlingStream).expectError().verify();
    assertEquals(1, counter.get());
  }

  private static class GenericException extends RuntimeException {
    public GenericException(Throwable cause) {
      super(cause);
    }
  }
}
