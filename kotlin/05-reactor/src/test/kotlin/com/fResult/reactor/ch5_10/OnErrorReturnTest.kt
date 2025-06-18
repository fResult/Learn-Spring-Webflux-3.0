package com.fResult.reactor.ch5_10

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import kotlin.test.Test

class OnErrorReturnTest {
  private val resultsInError = Flux.just(1, 2, 3).map(::failOn2)

  @Test
  fun onErrorReturn() {
    val integers = resultsInError.onErrorReturn(RuntimeException::class.java, 0)

    StepVerifier.create(integers).expectNext(1, 0).verifyComplete()
  }

  private fun failOn2(counter: Int): Int = if (counter == 2)
    throw RuntimeException("Error on value $counter")
  else counter
}
