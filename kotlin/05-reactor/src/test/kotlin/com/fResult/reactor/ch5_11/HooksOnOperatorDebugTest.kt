package com.fResult.reactor.ch5_11

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.test.StepVerifier
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.jvm.jvmName
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class HooksOnOperatorDebugTest {
  val log = LogManager.getLogger(HooksOnOperatorDebugTest::class.java)

  @Test
  fun onOperatorDebug() {
    Hooks.onOperatorDebug()

    val stackTrace = AtomicReference<String>()
    val errorStream = Flux.error<Int>(IllegalArgumentException("Oops!"))
      .checkpoint()
      .delayElements(1.milliseconds.toJavaDuration())

    StepVerifier.create(errorStream)
      .expectErrorMatches { ex ->
        stackTraceToString(ex).let {
          log.error("Error occurred: {}", it)
          stackTrace.set(it)
        }
        ex is IllegalArgumentException
      }
      .verify()
    assertTrue(stackTrace.get().contains("Flux.error ⇢ at ${HooksOnOperatorDebugTest::class.jvmName}"))
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
