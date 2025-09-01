package com.fResult.reactor.ch5_11;

import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
class HooksOnOperatorDebugTest {
  @Test
  void onOperatorDebug() {
    Hooks.onOperatorDebug();

    val stackTrace = new AtomicReference<String>();
    val errors =
        Flux.error(new IllegalArgumentException("Oops"))
            .checkpoint()
            .delayElements(Duration.ofMillis(1));

    StepVerifier.create(errors)
        .expectErrorMatches(
            ex -> {
              stackTrace.set(stackTraceToString(ex));
              return ex instanceof IllegalArgumentException;
            })
        .verify();

    assertTrue(
        stackTrace.get().contains("Flux.error ⇢ at " + HooksOnOperatorDebugTest.class.getName()));
  }

  private String stackTraceToString(Throwable ex) {
    try (val sw = new StringWriter();
        val pw = new PrintWriter(sw)) {
      ex.printStackTrace(pw);
      return sw.toString();
    } catch (IOException ioEx) {
      throw new RuntimeException(ioEx);
    }
  }
}
