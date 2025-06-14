package com.fResult.reactor.ch5_03

import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.test.StepVerifier
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class AsyncApiIntegrationTest {
  private val log = LogManager.getLogger(AsyncApiIntegrationTest::class.java)
  private val executorService = Executors.newFixedThreadPool(1)

  @Test
  fun async() {
    log.info("Starting async API integration test")
    val integers = Flux.create(launch(5))
    StepVerifier.create(integers.doFinally { executorService.shutdown() })
      .expectNextCount(5)
      .verifyComplete()
  }

  private fun launch(count: Int): (FluxSink<Int>) -> Unit = { integerFluxSink ->
    executorService.submit {
      val integer = AtomicInteger()
      Assertions.assertNotNull(integerFluxSink)
      while (integer.get() < count) {
        val random = (0L..1000L).random()
        integerFluxSink.next(integer.incrementAndGet())
        sleep(random)
      }
      log.info("Completed sending integers up to {}", count)
      integerFluxSink.complete()
    }
  }

  private fun sleep(ms: Long) {
    log.info("Sleeping for {} ms", ms)
    try {
      Thread.sleep(ms)
    } catch (ex: Exception) {
      log.error(ex)
    }
  }
}
