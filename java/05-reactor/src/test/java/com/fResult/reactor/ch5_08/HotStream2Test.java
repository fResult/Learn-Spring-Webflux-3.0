package com.fResult.reactor.ch5_08;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

@Log4j2
class HotStream2Test {
  @Test
  void hot() throws InterruptedException {
    val factor = 10;
    log.info("Start");
    val countDownLatch = new CountDownLatch(2);
    val live = Flux.range(0, 10).delayElements(Duration.ofMillis(factor)).share();

    val first = new ArrayList<Integer>();
    val second = new ArrayList<Integer>();

    live.doFinally(signalTypeConsumer(countDownLatch)).subscribe(first::add);
    Thread.sleep(factor * 2);
    live.doFinally(signalTypeConsumer(countDownLatch)).subscribe(second::add);

    countDownLatch.await(5, TimeUnit.SECONDS);

    assertTrue(first.size() > second.size());
  }

  private Consumer<SignalType> signalTypeConsumer(CountDownLatch cdl) {
    return signalType -> {
      if (signalType == SignalType.ON_COMPLETE) {
        try {
          cdl.countDown();
          log.info("await()...");
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    };
  }
}
