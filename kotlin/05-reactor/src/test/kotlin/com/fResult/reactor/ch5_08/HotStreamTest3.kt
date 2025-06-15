package com.fResult.reactor.ch5_08

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import kotlin.test.Test
import kotlin.test.assertEquals

class HotStreamTest3 {
  private val first = mutableListOf<Int>()
  private val second = mutableListOf<Int>()
  private val third = mutableListOf<Int>()

  @Test
  fun hot() {
    val pileOn = Flux.just(1, 2, 3)
      .publish()
      .autoConnect(3)
      .subscribeOn(Schedulers.immediate())

    pileOn.subscribe(first::add)
    assertEquals(0, first.size, "First subscriber should not have received any items yet")

    pileOn.subscribe(second::add)
    assertEquals(0, second.size, "Second subscriber should not have received any items yet")

    pileOn.subscribe(third::add)
    assertEquals(3, first.size, "First subscriber should have received all items")
    assertEquals(3, second.size, "Second subscriber should have received all items")
    assertEquals(3, third.size, "Third subscriber should have received all items")
  }
}
