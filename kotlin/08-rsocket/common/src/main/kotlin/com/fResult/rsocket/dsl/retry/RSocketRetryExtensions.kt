package com.fResult.rsocket.dsl.retry

import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.nio.channels.ClosedChannelException

/**
 * Create a [RetryBackoffSpec] that only retries on [ClosedChannelException].
 *
 * Defaults:
 *  - maxAttempts = 5
 *  - firstBackOff = 1.seconds
 *  - maxBackoff   = 32.seconds
 *
 * Example:
 * ```kt
 * flux.retryWhen(retryBackoffOnClosedChannel {
 *   maxAttempts = 10
 *   firstBackOff = 2.seconds
 *   maxBackoff   = 30.seconds
 * })
 * ```
 *
 * @param block optional builder to override defaults on [RetryConfigBuilder]
 * @return a backoff spec with 20% jitter and RuntimeException on exhaustion
 */
fun retryBackoffOnClosedChannel(
  block: RetryConfigBuilder.() -> Unit = {},
): RetryBackoffSpec {
  val cfg = RetryConfigBuilder()
    .apply(block)
    .build()

  return Retry.backoff(cfg.maxAttempts, cfg.firstBackOff)
    .maxBackoff(cfg.maxBackoff)
    .filter { it is ClosedChannelException }
    .jitter(0.2)
    .onRetryExhaustedThrow { _, _ ->
      RuntimeException("Retries exhausted")
    }
}
