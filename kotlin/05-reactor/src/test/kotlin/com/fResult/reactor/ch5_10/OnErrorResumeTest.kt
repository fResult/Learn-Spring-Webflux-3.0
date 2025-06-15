package com.fResult.reactor.ch5_10

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class OnErrorResumeTest {
  private val log = LogManager.getLogger(OnErrorResumeTest::class.java)

  private val resultsInError1 = Flux.just(1, 2, 3).map(::failOn2)

  @Test
  fun onErrorResume1() {
    val integers = resultsInError1.onErrorResume(RuntimeException::class.java, ::fallbackOnError1)
      .doOnComplete { log.info("Resume1 Completed") }

    StepVerifier.create(integers)
      .expectNext(1, 3, 2, 1) // Expect the fallback values after the error
      .verifyComplete()
  }

  private fun failOn2(counter: Int): Int = if (counter == 2)
    throw RuntimeException("Error on value $counter")
  else counter

  private fun fallbackOnError1(ex: RuntimeException): Flux<Int> {
    log.error("Fallback1 for error: {}", ex.message)

    return Flux.just(3, 2, 1)
      .doOnComplete { log.info("Fallback1 completed") }
  }
}

