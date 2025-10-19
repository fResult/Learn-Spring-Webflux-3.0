package com.fResult.orchestration.resilience4j

import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.reactor.retry.RetryOperator
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.Loggers
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Component
@Profile("retry")
class RetryClient(private val http: WebClient) {
  companion object {
    private val log = Loggers.getLogger(RetryClient::class.java)
  }

  private val config = RetryConfig.custom<Retry>()
    .waitDuration(1.seconds.toJavaDuration())
    .intervalFunction(IntervalFunction.ofExponentialBackoff(500.milliseconds.toJavaDuration(), 2.0))
    .maxAttempts(3)
    .build()

  private val retry = Retry.of("greetings-retry", config)
  private val uid = UUID.randomUUID().toString()

  @EventListener
  fun onApplicationReady(event: ApplicationReadyEvent) {
    GreetingClientUtils.getGreetingFor(http, uid, "retry")
      .transformDeferred(RetryOperator.of(retry))
      .subscribe(::onRetryReceived, ::onRetryError, ::onRetryCompleted)
  }

  private fun onRetryReceived(message: String?): Unit =
    log.info("Received: {}", message)

  private fun onRetryError(ex: Throwable): Unit =
    log.error("Error after retries: {}", ex.message)

  private fun onRetryCompleted() = {
    log.info("Retry sequence completed")
  }
}
