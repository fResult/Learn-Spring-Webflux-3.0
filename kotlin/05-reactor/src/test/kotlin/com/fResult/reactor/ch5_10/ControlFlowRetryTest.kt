package com.fResult.reactor.ch5_10

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.jvm.jvmName
import kotlin.test.Test

class ControlFlowRetryTest {
  private val log = LogManager.getLogger(ControlFlowRetryTest::class.java)

  @Test
  fun retry() {
    val errored = AtomicBoolean()
    val producer = Flux.create(simulatedErrorThenEmitHello(errored))

    val retryOnError = producer.retry()
    StepVerifier.create(retryOnError).expectNext("Hello, World!").verifyComplete()
  }

  private fun simulatedErrorThenEmitHello(errored: AtomicBoolean): (FluxSink<String>) -> Unit = { stringSink ->
    val isFirstTime = errored.compareAndSet(false, true)

    if (isFirstTime) {
      log.warn("Emitting error: {}", RuntimeException::class.jvmName)
      stringSink.error(RuntimeException("Simulated error"))
    } else {
      val message = "Hello, World!"
      log.info("Emitting {}", message)
      stringSink.next(message)
      stringSink.complete()
    }
  }
}
