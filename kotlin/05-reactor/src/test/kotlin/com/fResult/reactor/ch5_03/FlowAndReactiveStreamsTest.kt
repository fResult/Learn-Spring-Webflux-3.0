package com.fResult.reactor.ch5_03

import org.junit.jupiter.api.Test
import org.reactivestreams.FlowAdapters
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.Flow

class FlowAndReactiveStreamsTest {
  @Test
  fun convert(): Unit {
    val original = Flux.range(0, 10)
    val rangeOfIntegersAsJdk9Flow: Flow.Publisher<Int> = FlowAdapters.toFlowPublisher(original)
    val rangeOfIntegersAsReactiveStream: Publisher<Int> = FlowAdapters.toPublisher(rangeOfIntegersAsJdk9Flow)

    StepVerifier.create(original).expectNextCount(10).verifyComplete()
    StepVerifier.create(rangeOfIntegersAsReactiveStream).expectNextCount(10).verifyComplete()
  }
}
