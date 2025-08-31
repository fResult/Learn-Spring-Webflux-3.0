package com.fResult.reactor.ch5_11;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
class CheckpointTest {
  @Test
  void checkpoint() {
    val stackTrace = new AtomicReference<String>();
    val checkpoint =
        Flux.error(new IllegalArgumentException("Oops"))
            .checkpoint()
            .delayElements(Duration.ofMillis(1));

    StepVerifier.create(checkpoint)
        .expectErrorMatches(
            ex -> {
              stackTrace.set(stackTraceToString(ex));
              return ex instanceof IllegalArgumentException;
            })
        .verify();

    assertTrue(stackTrace.get().contains("Error has been observed at the following site(s):"));
  }

  @SneakyThrows(IOException.class)
  private String stackTraceToString(Throwable ex) {
    try (val sw = new StringWriter();
        val pw = new PrintWriter(sw)) {

      ex.printStackTrace(pw);
      return sw.toString();
    }
  }
}
