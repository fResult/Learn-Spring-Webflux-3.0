package com.fResult.reactor.ch5_07

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.jvm.jvmName
import kotlin.test.Test
import kotlin.test.assertEquals

class SchedulersSubscribeOnTest {
  private val log = LogManager.getLogger(SchedulersSubscribeOnTest::class.java)

  @Test
  fun subscribeOn() {
    val rsbThreadName = SchedulersSubscribeOnTest::class.jvmName
    log.info("Test: SchedulersSubscribeOnTest#subscribeOn")
    val map = ConcurrentHashMap<String, AtomicInteger>()
    val executor = Executors.newFixedThreadPool(5, countingThreadFactory(rsbThreadName, map))
    val scheduler = Schedulers.fromExecutor(executor)
    val integer = Mono.just(1).doOnNext(log::info).subscribeOn(scheduler).doFinally { map.forEach(::logKeyValue) }

    StepVerifier.create(integer).expectNextCount(1).verifyComplete()
    val atomicInteger = map.getOrDefault(rsbThreadName, AtomicInteger())
    assertEquals(atomicInteger.get(), 1)
  }

  /**
   * Creates a [ThreadFactory] that produces threads with the given [threadName],
   * and **increments a counter** in the provided [map] every time a thread runs a task.
   *
   * ## Side Effects
   * - Mutates the provided [map] by inserting/updating entries.
   * - Each thread execution increments the associated [AtomicInteger] counter.
   *
   * ## Thread Safety
   * - Assumes [map] is thread-safe (e.g., [ConcurrentHashMap]).
   *
   * @param threadName the name assigned to each created thread
   * @param map a shared, mutable map where thread execution counts will be stored
   * @return a [ThreadFactory] with side effects on [map]
   */
  private fun countingThreadFactory(
    threadName: String,
    map: ConcurrentHashMap<String, AtomicInteger>,
  ): ThreadFactory =
    ThreadFactory { runnable ->
      val wrapper = Runnable {
        val key = Thread.currentThread().name
        val result = map.computeIfAbsent(key) { AtomicInteger() }
        result.incrementAndGet()
        runnable.run()
      }
      Thread(wrapper, threadName)
    }

  fun logKeyValue(key: String, value: AtomicInteger) =
    log.info("Key: [{}], Value: [{}]", key, value)
}
