package com.fResult.reactor.ch5_10

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OnErrorMapTest {
  private val log = LogManager.getLogger(OnErrorMapTest::class.java)

  companion object {
    private class GenericException(message: String) : RuntimeException(message)
  }

  @Test
  fun onErrorMap() {
    val counter = AtomicInteger()
    val resultsInError = Flux.error<Int>(IllegalArgumentException("Oops!"))
    val errorHandlingStream = resultsInError
      .onErrorMap(IllegalArgumentException::class.java, ::toGenericException)
      .doOnError { counter.incrementAndGet() }

    StepVerifier.create(errorHandlingStream).verifyErrorSatisfies(::assertExceptionType)
  }

  private fun toGenericException(ex: IllegalArgumentException): GenericException {
    log.warn("Error occurred: {}", ex.message)

    return GenericException("Mapped: ${ex.message}")
  }

  private fun assertExceptionType(ex: Throwable) {
    assertFalse(ex is IllegalArgumentException)
    assertTrue(ex is RuntimeException)
    assertTrue(ex is GenericException)
    log.info("Exception type is correct: {}", ex.javaClass.simpleName)
  }
}
