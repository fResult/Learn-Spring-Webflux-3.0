package com.fResult.reactor.ch5_10

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.test.StepVerifier
import kotlin.reflect.jvm.jvmName
import kotlin.test.Test

class ControlFlowRetryTest {
  private val log = LogManager.getLogger(ControlFlowRetryTest::class.java)

  @Test
  fun retry() {
    val producer = Flux.create(simulatedError2TimesThenEmitHello())
    val retryOnError = producer.retry()

    StepVerifier.create(retryOnError).expectNext("Hello, World!").verifyComplete()
  }

  private fun simulatedError2TimesThenEmitHello(): (FluxSink<String>) -> Unit {
    var tryingCounter = 0

    return { stringSink ->
      val lessThan2 = ++tryingCounter <= 2

      if (lessThan2) {
        log.error("Emitting error: {} {} time(s)", RuntimeException::class.jvmName, tryingCounter)
        stringSink.error(RuntimeException("Simulated error: $tryingCounter time(s)"))
      } else {
        val message = "Hello, World!"
        log.info("Emitting {}", message)
        stringSink.next(message)
        stringSink.complete()
      }
    }
  }
}
