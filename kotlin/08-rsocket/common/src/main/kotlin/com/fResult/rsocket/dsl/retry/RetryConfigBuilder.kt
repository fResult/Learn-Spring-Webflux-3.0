package com.fResult.rsocket.dsl.retry

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class RetryConfigBuilder {
  var maxAttempts: Int = 5
  var firstBackOff: Duration = 1.seconds
  var maxBackoff: Duration = 32.seconds

  fun build() = RetryConfig(
    maxAttempts = maxAttempts.toLong(),
    firstBackOff = firstBackOff.toJavaDuration(),
    maxBackoff = maxBackoff.toJavaDuration(),
  )
}
