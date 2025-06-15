package com.fResult.reactor.ch5_08

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Sinks
import kotlin.test.Test
import kotlin.test.assertEquals

class HotStreamTest1 {
  private val log = LogManager.getLogger(HotStreamTest1::class.java)

  @Test
  fun hot() {
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()

    // val sink = Sinks.many().replay().all<Int>()
    val sink = Sinks.many().multicast().onBackpressureBuffer<Int>(2)

    sink.apply {
      val flux = asFlux()
      flux.doOnNext(logInfo("First")).subscribe(first::add)
      tryEmitNext(1)
      tryEmitNext(2)

      flux.doOnNext(logInfo("Second")).subscribe(second::add)
      tryEmitNext(3)
      tryEmitComplete()
    }

    assertEquals(first.size, 3)
    assertEquals(second.size, 1)
  }

  private fun logInfo(name: String): (Int) -> Unit {
    return { log.info("[$name]: $it") }
  }
}
