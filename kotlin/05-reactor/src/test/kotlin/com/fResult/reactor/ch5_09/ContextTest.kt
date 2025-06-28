package com.fResult.reactor.ch5_09

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Signal
import reactor.core.publisher.SignalType
import reactor.util.context.Context
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class ContextTest {
  private val log = LogManager.getLogger(ContextTest::class.java)

  @Test
  fun context() {
    val observedContextValues = ConcurrentHashMap<String, AtomicInteger>()
    val max = 3
    val countDownLatch = CountDownLatch(max)
    val contextKey = "key1"
    val expectedContext = Context.of(contextKey, "value1")

    val contextAwareFlux = Flux.range(0, max)
      .delayElements(1L.milliseconds.toJavaDuration())
      .doOnEach(assertAndCountContextOnNextSignal(observedContextValues, contextKey))
      .contextWrite(expectedContext)

    contextAwareFlux.subscribe(
      logAndCountDown(countDownLatch),
      { ex -> log.error("Error in subscriber: {}", ex.message) },
      { log.info("Completed successfully") }
    )

    countDownLatch.await()

    val contextAtomicInt = observedContextValues.getOrDefault(contextKey, AtomicInteger())
    assertEquals(max, contextAtomicInt.get())
  }

  /**
   * Verifies that each `onNext` signal in a [Flux] stream contains the expected context value.
   *
   * ## Behavior
   * - For every `onNext` [Signal], this function:
   *   - Retrieves the context from the signal
   *   - Asserts that the value associated with key `"key1"` exists and equals `"value1"`
   *   - Increments a counter in the provided [contextObservationMap] to track signal occurrences
   *
   * ## Side Effects
   * - Mutates the shared [contextObservationMap] by incrementing the counter for key `"key1"` on each `onNext` signal.
   *
   * ## Thread Safety
   * - Assumes [contextObservationMap] is a thread-safe map (e.g., [ConcurrentHashMap]) and its values are [AtomicInteger] instances.
   *
   * ## Use Case
   * - Designed for testing and diagnostics in reactive pipelines that leverage Reactor's [Context] for passing metadata.
   * - Useful for ensuring that context propagation behaves as expected in reactive flows.
   *
   * @param contextObservationMap a thread-safe map used to count how many times the expected context was observed.
   * @return a lambda to be used with [Flux.doOnEach] or similar signal inspection operators.
   */
  private fun assertAndCountContextOnNextSignal(
    contextObservationMap: ConcurrentHashMap<String, AtomicInteger>,
    key: String,
  ): (Signal<Int>) -> Unit = { intSignal ->
    val currentContext = intSignal.contextView

    if (intSignal.type == SignalType.ON_NEXT) {
      val key1 = currentContext.get<String>(key)
      assertNotNull(key1)
      assertEquals(key1, "value1", "Context value should match")
      contextObservationMap
        .computeIfAbsent(key) { AtomicInteger() }
        .incrementAndGet()
    }
  }

  private fun logAndCountDown(latch: CountDownLatch): (Int) -> Unit = { value ->
    try {
      log.info("Received value: {}", value)
      latch.countDown()
      log.info("Counted down, remaining: ${latch.count}")
    } catch (ex: Exception) {
      throw RuntimeException("Error in count down", ex)
    }
  }
}
