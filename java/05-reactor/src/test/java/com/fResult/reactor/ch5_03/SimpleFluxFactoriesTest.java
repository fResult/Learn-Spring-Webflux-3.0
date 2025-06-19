import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
public class SimpleFluxFactoriesTest {
  @Test
  void simple() {
    Publisher<Integer> rangeOfIntegers = Flux.range(0, 10);
    StepVerifier.create(rangeOfIntegers).expectNextCount(10).verifyComplete();
  }
}
