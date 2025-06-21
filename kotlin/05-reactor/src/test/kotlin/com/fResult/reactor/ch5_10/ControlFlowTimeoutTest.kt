package com.fResult.reactor.ch5_10

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.TimeoutException
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class ControlFlowTimeoutTest {
  private val log = LogManager.getLogger(ControlFlowTimeoutTest::class.java)

  @Test
  fun timeout() {
    val ids =
      Flux.just(1, 2, 3)
        .delayElements(1.seconds.toJavaDuration())
        .timeout(500.milliseconds.toJavaDuration())
        .onErrorResume(::fallbackOnTimeout)

    StepVerifier.create(ids).expectNext(0).verifyComplete()
  }

  private fun fallbackOnTimeout(ex: Throwable): Flux<Int> {
    log.warn("Timeout occurred: {}", ex.message)
    assertTrue(ex is TimeoutException, "Expected TimeoutException, but got ${ex::class.simpleName}")
    return Flux.just(0)
  }
}
