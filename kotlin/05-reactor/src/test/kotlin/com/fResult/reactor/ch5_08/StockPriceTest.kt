package com.fResult.reactor.ch5_08

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Sinks
import kotlin.concurrent.thread
import kotlin.test.Test

class StockPriceTest {
  private val log = LogManager.getLogger(StockPriceTest::class.java)

  @Test
  fun stockPrice() {
    val stockSink = Sinks.many().multicast().onBackpressureBuffer<Double>()
    val stocks = stockSink.asFlux()

    thread {
      var price = 100.0
      while (true) {
        price += (-1..1).random() * 0.5 // Simulate price change
        stockSink.tryEmitNext(price)
        Thread.sleep(1000) // Update every second
      }
    }

    stocks.subscribe { price -> log.info("Client A sees stock price: {}", price) }

    Thread.sleep(3000)
    stocks.subscribe { price -> log.info("Client B sees stock price: {}", price) }

    Thread.sleep(5000)
    stockSink.tryEmitComplete() // Complete the sink after some time
  }
}
