package com.fResult.orchestration.resilience4j

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.Loggers
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
@Profile("rate-limiter")
class RateLimiterClient(private val http: WebClient) {
  private val uid = UUID.randomUUID().toString()
  private val rateLimiter = RateLimiter.of("greetings-rate-limiter", ::rateLimiterConfig)

  companion object {
    private val log = Loggers.getLogger(RateLimiterClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting Rate Limiter client with UID={}", uid)
    val max = 20
    val countDownLatch = CountDownLatch(max)
    val resultCounter = AtomicInteger()
    val errorCounter = AtomicInteger()

    (0..max).forEach { idx ->
      log.info("Submitting request #{} for Rate Limiter client with UID={}", idx, uid)
      buildRequest(countDownLatch, resultCounter, errorCounter, rateLimiter, idx)
        .subscribe()
    }

    countDownLatch.await()
    log.info("Final results for Rate Limiter client with UID=$uid: Successful Calls=${resultCounter.get()}, Errors=${errorCounter.get()}")
  }

  private fun rateLimiterConfig(): RateLimiterConfig =
    RateLimiterConfig.custom()
      .limitForPeriod(10)
      .limitRefreshPeriod(1.seconds.toJavaDuration())
      .build()

  private fun buildRequest(
    countDownLatch: CountDownLatch,
    resultCounter: AtomicInteger,
    errorCounter: AtomicInteger,
    rateLimiter: RateLimiter,
    idx: Int,
  ): Mono<String?> = GreetingClientUtils.getGreetingFor(http, uid, "ok")
    .transformDeferred(RateLimiterOperator.of(rateLimiter))
    .doOnError(recordGreetingFailed(errorCounter, idx))
    .doOnNext(recordGreetingSucceed(resultCounter, idx))
    .doOnTerminate(countDownLatch::countDown)

  private fun recordGreetingFailed(errorCounter: AtomicInteger, idx: Int): (Throwable) -> Unit = { ex ->
    errorCounter.incrementAndGet()
    log.warn("Oops! Request #$idx for Rate Limiter client with UID={} failed with error: {}", uid, ex.message)
  }

  private fun recordGreetingSucceed(resultCounter: AtomicInteger, idx: Int): (String?) -> Unit = { reply ->
    resultCounter.incrementAndGet()
    log.info("Received reply for request #{} for Rate Limiter client with UID={}: {}", idx, uid, reply)
  }
}
