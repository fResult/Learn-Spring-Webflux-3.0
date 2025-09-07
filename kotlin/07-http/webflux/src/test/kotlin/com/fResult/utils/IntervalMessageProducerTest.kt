package com.fResult.utils

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class IntervalMessageProducerTest {
  @Test
  fun produce(): Unit {
    val actualResult = IntervalMessageProducer.produce(2)

    StepVerifier.create(actualResult).expectNext("# 1").expectNext("# 2").verifyComplete()
  }
}
