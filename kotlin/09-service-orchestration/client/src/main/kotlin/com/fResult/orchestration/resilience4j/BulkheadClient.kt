package com.fResult.orchestration.resilience4j

import io.github.resilience4j.bulkhead.Bulkhead
import io.github.resilience4j.bulkhead.BulkheadConfig
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import reactor.util.Loggers
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Component
@Profile("bulkhead")
class BulkheadClient(private val http: WebClient) {
  private val uid = UUID.randomUUID().toString()
  private val availableProcessors = Runtime.getRuntime().availableProcessors()
  private val maxCalls = availableProcessors / 2
  private val bulkhead = Bulkhead.of("greetings-bulkhead", ::bulkheadConfig)

  companion object {
    private val log = Loggers.getLogger(BulkheadClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info(
      "There are {} available, therefore there should be {} in the pool",
      availableProcessors,
      availableProcessors
    )

    val immediate = Schedulers.immediate()
    (1..availableProcessors).forEach { n ->
      buildRequest(immediate, n).subscribe { log.info("Received response: {}", it)}
    }
  }

  private fun buildRequest(scheduler: Scheduler, n: Int): Mono<String?> {
    log.info("Bulkhead attempt: #{} for {}", n, uid)

    return GreetingClientUtils.getGreetingFor(http, uid, "ok")
      .transformDeferred(BulkheadOperator.of(bulkhead))
      .publishOn(scheduler)
      .subscribeOn(scheduler)
      .onErrorResume { ex ->
        log.error(
          "The bulkhead kicked in for the attempt #{}. Receive the following exception: {}",
          n,
          ex::class.simpleName
        )
        Mono.empty<String>()
      }.onErrorStop()
  }

  private fun bulkheadConfig(): BulkheadConfig = BulkheadConfig.custom()
    .writableStackTraceEnabled(true)
    .maxConcurrentCalls(maxCalls)
    .maxWaitDuration(5.milliseconds.toJavaDuration())
    .build()
}
