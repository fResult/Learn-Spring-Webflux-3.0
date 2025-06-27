package com.fResult.reactor.ch5_03;

import java.util.concurrent.Flow;
import org.junit.jupiter.api.Test;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FlowAndReactiveStreamsTest {
  @Test
  void convert() {
    final var original = Flux.range(0, 10);
    Flow.Publisher<Integer> rangeOfIntegersAsJdk9Flow = FlowAdapters.toFlowPublisher(original);
    Publisher<Integer> rangeOfIntegersAsReactiveStream =
        FlowAdapters.toPublisher(rangeOfIntegersAsJdk9Flow);

    StepVerifier.create(original).expectNextCount(10).verifyComplete();
    StepVerifier.create(rangeOfIntegersAsReactiveStream).expectNextCount(10).verifyComplete();

    final var rangeOfIntegersAsReactorFluxAgain =
        JdkFlowAdapter.flowPublisherToFlux(rangeOfIntegersAsJdk9Flow);
    StepVerifier.create(rangeOfIntegersAsReactorFluxAgain).expectNextCount(10).verifyComplete();
  }
}
