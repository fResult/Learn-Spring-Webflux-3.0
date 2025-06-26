package com.fResult.reactor.ch5_11

import org.apache.logging.log4j.LogManager
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.util.ServiceLoader
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class BlockHoundTest {
  private val log = LogManager.getLogger(BlockHoundTest::class.java)

  companion object {
    private val BLOCK_HOUND = AtomicBoolean()

    private class BlockingCallError(msg: String) : Error(msg)
  }

  @BeforeTest
  fun setUp() {
    log.info("BlockHound test started, blocking calls are now prohibited.")
    BLOCK_HOUND.set(true)
    val integrations = mutableListOf<BlockHoundIntegration>()
    val services = ServiceLoader.load(BlockHoundIntegration::class.java)
    services.forEach(integrations::add)

    integrations.add { builder ->
      builder.blockingMethodCallback { blockingMethod ->
        if (BLOCK_HOUND.get()) {
          log.error("Blocking call detected at: $blockingMethod")

          throw BlockingCallError(blockingMethod.toString())
        }
      }
    }

    BlockHound.install(*integrations.toTypedArray());
  }

  @AfterTest
  fun after() {
    BLOCK_HOUND.set(false)
    log.info("BlockHound test completed, blocking calls are now allowed.")
  }

  @Test
  fun ok() {
    StepVerifier.create(buildBlockingMono().subscribeOn(Schedulers.boundedElastic()))
      .expectNext(1L)
      .verifyComplete()
  }

  @Test
  fun notOk() {
    StepVerifier.create(buildBlockingMono().subscribeOn(Schedulers.parallel()))
      .expectErrorMatches(::isBlockingCallError)
      .verify()
  }

  private fun buildBlockingMono() = Mono.just(1L).doOnNext { block() }

  private fun block() = Thread.sleep(1000)

  private fun isBlockingCallError(ex: Throwable) = ex is BlockingCallError
}
