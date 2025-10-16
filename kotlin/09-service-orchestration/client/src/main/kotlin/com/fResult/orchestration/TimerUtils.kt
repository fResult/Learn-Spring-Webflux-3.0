package com.fResult.orchestration

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.Logger
import reactor.util.Loggers
import java.util.concurrent.atomic.AtomicLong

abstract class TimerUtils {
  companion object {
    private val log: Logger = Loggers.getLogger(TimerUtils::class.java)

    fun <T> cache(cache: Mono<T>): Mono<T> =
      cache.doOnNext { c -> log.debug("Receiving ${c.toString()}") }.cache()

    fun <T> cache(cache: Flux<T>): Flux<T> =
      cache.doOnNext { c -> log.debug("Receiving ${c.toString()}") }.cache()

    fun <T> monitor(config: Mono<T>): Mono<T> {
      val start = AtomicLong()

      return config.doOnError { ex -> log.error("Oops!", ex) }
        .doOnSubscribe { subscription -> start.set(System.currentTimeMillis()) }
        .doOnNext { greeting -> log.info("Total time: {}", System.currentTimeMillis() - start.get()) }
    }

    fun <T> monitor(configs: Flux<T>): Flux<T> {
      val start = AtomicLong()

      return configs.doOnError { ex -> log.error("Oops!", ex) }
        .doOnSubscribe { subscription -> start.set(System.currentTimeMillis()) }
        .doOnNext { greeting -> log.info("Total time: {}", System.currentTimeMillis() - start.get()) }
    }
  }
}
