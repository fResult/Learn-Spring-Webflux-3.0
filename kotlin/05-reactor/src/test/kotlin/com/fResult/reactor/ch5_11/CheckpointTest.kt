package com.fResult.reactor.ch5_11

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class CheckpointTest {
  @Test
  fun checkpoint() {
    val stackTrace = AtomicReference<String>()

    val checkpoint = Flux.error<String>(IllegalArgumentException("Oops!"))
      .checkpoint()
      .delayElements(1.milliseconds.toJavaDuration())

    StepVerifier.create(checkpoint)
      .expectErrorMatches { ex ->
        stackTrace.set(stackTraceToString(ex))

        ex is IllegalArgumentException
      }
      .verify()

    assertTrue(stackTrace.get().contains("Error has been observed at the following site(s):"))
  }

  private fun stackTraceToString(ex: Throwable) = StringWriter().use(stackTraceWriteHandler(ex))

  private fun stackTraceWriteHandler(ex: Throwable): (StringWriter) -> String = { sw ->
    PrintWriter(sw).use(captureStackTraceToString(ex, sw))
  }

  private fun captureStackTraceToString(ex: Throwable, sw: StringWriter): (PrintWriter) -> String = { pw ->
    ex.printStackTrace(pw)
    sw.toString()
  }
}
