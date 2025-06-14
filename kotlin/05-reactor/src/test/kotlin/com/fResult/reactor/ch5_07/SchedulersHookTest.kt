package com.fResult.reactor.ch5_07

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class SchedulersHookTest {
  private val log = LogManager.getLogger(SchedulersHookTest::class)

  companion object {
    const val MY_HOOK = "my hook"
  }

  @Test
  fun onScheduleHook() {
    log.info("Test: SchedulersHookTest#onScheduleHook")
    val counter = AtomicInteger()
    Schedulers.onScheduleHook(MY_HOOK, wrapRunnableWithCounting(counter))
    val integers = Flux.just(1, 2, 3).delayElements(1.milliseconds.toJavaDuration()).subscribeOn(Schedulers.immediate())

    StepVerifier.create(integers).expectNext(1, 2, 3).verifyComplete()
    assertEquals(3, counter.get(), "Counter should be 3 as we have 3 elements")
  }

  /**
   * Returns a decorator function that wraps a [Runnable] to:
   * - **Increment the given [counter]** before execution.
   * - Log the current thread name before and after executing the [Runnable].
   *
   * ## Side Effects
   * - Mutates the provided [counter] by incrementing it once for each wrapped execution.
   * - Produces logging output on each execution.
   *
   * ## Thread Safety
   * - Assumes [counter] is thread-safe (e.g., [AtomicInteger]).
   *
   * ## Use Case
   * - Intended for use with [Schedulers.onScheduleHook] to monitor and count task scheduling.
   *
   * @param counter a shared, thread-safe counter incremented on each execution
   * @return a function that wraps a [Runnable] with counting and logging side effects
   */
  private fun wrapRunnableWithCounting(counter: AtomicInteger): (Runnable) -> Runnable {
    return { runnable ->
      Runnable {
        val threadName = Thread.currentThread().name
        counter.incrementAndGet()
        log.info("Before execution on thread: {}", threadName)
        runnable.run()
        log.info("After execution on thread: {}", threadName)
      }
    }
  }
}
