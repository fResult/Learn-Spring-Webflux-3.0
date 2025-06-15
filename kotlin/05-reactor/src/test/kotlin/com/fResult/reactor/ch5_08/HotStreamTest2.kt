package com.fResult.reactor.ch5_08

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.SignalType
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class HotStreamTest2 {
  private val log = LogManager.getLogger(HotStreamTest2::class.java)

  @Test
  fun hot() {
    log.info("Start")

    val factor = 10L
    val countDownLatch = CountDownLatch(2)
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()
    val live = Flux.range(0, 10)
      .delayElements(factor.milliseconds.toJavaDuration())
      .doOnNext { log.info("Emit: {}", it) }
      .share()

    live.doFinally(onCompleteCountDown(countDownLatch)).subscribe(first::add) { ex ->
      log.error("Error in first subscriber: {}", ex.message)
    }

    Thread.sleep(factor * 2)

    live.doFinally(onCompleteCountDown(countDownLatch)).subscribe(second::add) { ex ->
      log.error("Error in second subscriber: {}", ex.message)
    }

    countDownLatch.await(5L, TimeUnit.SECONDS)
    assertTrue(first.size > second.size)
    log.info("stop")
  }

  private fun onCompleteCountDown(latch: CountDownLatch): (SignalType) -> Unit = { signalType ->
    log.info("Signal Type: {}", signalType)
    if (signalType == SignalType.ON_COMPLETE) {
      try {
        latch.countDown()
        log.info("await()...")
      } catch (ex: Exception) {
        throw RuntimeException("Error in signal type consumer", ex)
      }
    }
  }
}
