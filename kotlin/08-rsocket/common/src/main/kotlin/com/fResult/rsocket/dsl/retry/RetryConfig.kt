package com.fResult.rsocket.dsl.retry

import java.time.Duration

data class RetryConfig(
  val maxAttempts: Long,
  val firstBackOff: Duration,
  val maxBackoff: Duration,
)
