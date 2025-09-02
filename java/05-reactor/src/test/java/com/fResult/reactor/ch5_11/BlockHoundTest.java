package com.fResult.reactor.ch5_11;

import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
class BlockHoundTest {
  private static final AtomicBoolean BLOCK_HOUND = new AtomicBoolean();

  private static class BlockingCallError extends Error {
    public BlockingCallError(String msg) {
      super(msg);
    }
  }

  @BeforeEach
  void setUp() {
    BLOCK_HOUND.set(true);
    val integrations = new ArrayList<BlockHoundIntegration>();
    val services = ServiceLoader.load(BlockHoundIntegration.class);
    services.forEach(integrations::add);

    integrations.add(
        builder -> {
          builder.blockingMethodCallback(
              blockingMethod -> {
                if (BLOCK_HOUND.get()) {
                  log.error("Blocking method detected at: {}", blockingMethod);

                  throw new BlockingCallError(blockingMethod.toString());
                }
              });
        });

    BlockHound.install(integrations.toArray(BlockHoundIntegration[]::new));
  }

  @Test
  void ok() {
    StepVerifier.create(buildBlockingMono().subscribeOn(Schedulers.boundedElastic()))
        .expectNext(1L)
        .verifyComplete();
  }

  @Test
  void notOk() {
    StepVerifier.create(buildBlockingMono().subscribeOn(Schedulers.parallel()))
        .expectErrorMatches(this::isBlockingCallError)
        .verify();
  }

  @AfterEach
  void tearDown() {
    BLOCK_HOUND.set(false);
  }

  private Mono<Long> buildBlockingMono() {
    return Mono.just(1L).doOnNext(ignored -> block());
  }

  @SneakyThrows(InterruptedException.class)
  private void block() {
    Thread.sleep(1000);
  }

  private boolean isBlockingCallError(Throwable ex) {
    return ex instanceof BlockingCallError;
  }
}
