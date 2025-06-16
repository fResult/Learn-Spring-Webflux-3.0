package com.fResult.reactor.ch5_10

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class OnErrorResumeTest {
  private val log = LogManager.getLogger(OnErrorResumeTest::class.java)

  private val resultsInError1 = Flux.just(1, 2, 3).map(::failOn2)
  private val resultsInError2 = Flux.just(1L, 2L, 3L).flatMap(::failOn2)

  @Test
  fun onErrorResume1() {
    val integers = resultsInError1.onErrorResume(RuntimeException::class.java, ::fallbackOnError1)
      .doOnComplete { log.info("Resume1 Completed") }

    StepVerifier.create(integers)
      .expectNext(1, 3, 2, 1) // Expect the fallback values after the error
      .verifyComplete()
  }

  @Test
  fun onErrorResume2() {
    val longs = resultsInError2.onErrorResume(RuntimeException::class.java, ::fallbackOnError2)
      .doOnComplete { log.info("Resume2 Completed") }

    StepVerifier.create(longs)
      .expectNext(1L, 3L, 2L, 1L) // Expect the fallback values after the error
      .verifyComplete()
  }

  private fun failOn2(counter: Int): Int = if (counter == 2)
    throw RuntimeException("Error on value $counter")
  else counter

  private fun failOn2(counter: Long): Flux<Long> = if (counter == 2L)
    Flux.error(RuntimeException("Error on value $counter"))
  else
    Flux.just(counter)

  private fun fallbackOnError1(ex: RuntimeException): Flux<Int> {
    log.error("Fallback1 for error: {}", ex.message)

    return Flux.just(3, 2, 1)
      .doOnComplete { log.info("Fallback1 completed") }
  }

  private fun fallbackOnError2(ex: RuntimeException): Flux<Long> {
    log.error("Fallback2 for error: {}", ex.message)

    return Flux.just(3L, 2L, 1L)
      .doOnComplete { log.info("Fallback2 completed") }
  }
}

