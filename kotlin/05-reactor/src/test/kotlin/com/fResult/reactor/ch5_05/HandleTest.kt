package com.fResult.reactor.ch5_05

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.stream.Stream
import kotlin.test.Test

class HandleTest {
  private val log = LogManager.getLogger(HandleTest::class)

  @Test
  fun handle() {
    StepVerifier.create(handle(5, 4))
      .expectNext(0, 1, 2, 3)
      .expectError(IllegalArgumentException::class.java)
      .verify()

    StepVerifier.create(handle(3, 3)).expectNext(0, 1, 2).verifyComplete()
  }

  private fun handle(max: Int, numberToError: Int): Flux<Int> = Flux.range(0, max)
    .handle { value, sink ->
      val upTo = Stream.iterate(0, { it < numberToError }) { it + 1 }.toList()

      if (upTo.contains(value)) {
        sink.next(value)
        return@handle
      }
      if (value == numberToError) {
        sink.error(IllegalArgumentException("No 4 for you!"))
        return@handle
      }
      sink.complete()
    }
}
