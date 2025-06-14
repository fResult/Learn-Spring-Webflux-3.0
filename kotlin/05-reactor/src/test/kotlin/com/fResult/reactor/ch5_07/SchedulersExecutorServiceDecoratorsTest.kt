package com.fResult.reactor.ch5_07

import org.aopalliance.intercept.MethodInterceptor
import org.apache.logging.log4j.LogManager
import org.springframework.aop.framework.ProxyFactoryBean
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class SchedulersExecutorServiceDecoratorsTest {
  private val log = LogManager.getLogger(SchedulersExecutorServiceDecoratorsTest::class)
  private val methodInvocationCounts = AtomicInteger()

  companion object {
    private const val RSB = "rsb"
  }

  @BeforeTest
  fun before() {
    log.info("Before test: SchedulersExecutorServiceDecoratorsTest")
    Schedulers.resetFactory()
    Schedulers.addExecutorServiceDecorator(RSB, ::scheduleCountingDecorator)
  }

  @Test
  fun changeDefaultDecorator() {
    log.info("Test: changeDefaultDecorator")
    val integers = Flux.just<Int>(1).delayElements(1L.milliseconds.toJavaDuration())
    StepVerifier.create(integers).thenAwait(10.milliseconds.toJavaDuration()).expectNextCount(1).verifyComplete()
    assertEquals(1, methodInvocationCounts.get())
  }

  @AfterTest
  fun after() {
    log.info("After test: SchedulersExecutorServiceDecoratorsTest")
    Schedulers.resetFactory()
    Schedulers.removeExecutorServiceDecorator(RSB)
  }

  private fun scheduleCountingDecorator(scheduler: Scheduler, executorService: ScheduledExecutorService): ScheduledExecutorService? {
    try {
      return ProxyFactoryBean()
        .apply {
          setProxyInterfaces(arrayOf(ScheduledExecutorService::class.java))
          addAdvice(interceptAndCountScheduleMethods)
          isSingleton = true
          setTarget(executorService)
        }
        .let { it.`object` as ScheduledExecutorService }
    } catch (ex: Exception) {
      log.error(ex)
    }

    return null
  }

  /**
   * A [MethodInterceptor] that intercepts method calls on [ScheduledExecutorService]
   * and **increments [methodInvocationCounts]** when the invoked method's name starts with `"schedule"`.
   *
   * ## Side Effects
   * - Mutates the shared [methodInvocationCounts] counter on every intercepted method
   *   whose name starts with `"schedule"`.
   * - Logs the method name when a match is found and the counter is incremented.
   *
   * ## Thread Safety
   * - Assumes [methodInvocationCounts] is a thread-safe counter (e.g., [AtomicInteger]).
   *
   * ## Use Case
   * - Intended to be used as advice for proxying [ScheduledExecutorService] instances
   *   via Spring's [org.springframework.aop.framework.ProxyFactoryBean], to count scheduled task invocations.
   *
   * @see startWithSchedule for the matching logic
   */
  private val interceptAndCountScheduleMethods = MethodInterceptor { methodInvocation ->
    val methodName = methodInvocation.method.name
    methodName.takeIf(::startWithSchedule)
      ?.also {
        methodInvocationCounts.incrementAndGet()
        log.info("methodName: [{}] incrementing...", it)
      }
    methodInvocation.proceed()
  }

  private fun startWithSchedule(methodName: String): Boolean = methodName.startsWith("schedule")
}
