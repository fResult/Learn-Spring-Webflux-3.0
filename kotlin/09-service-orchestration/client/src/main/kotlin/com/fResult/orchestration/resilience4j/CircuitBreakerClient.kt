package com.fResult.orchestration.resilience4j

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.util.Loggers
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Profile("circuit-breaker")
@Component
class CircuitBreakerClient(private val http: WebClient) {
  companion object {
    private val log = Loggers.getLogger(CircuitBreakerClient::class.java)
  }

  private val uid = UUID.randomUUID().toString()
  private val circuitBreaker = CircuitBreaker.of("greetings-circuit-breaker", ::circuitBreakerConfig)

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    log.info("Starting Circuit Breaker client with UID=$uid")
    GreetingClientUtils.getGreetingFor(http, "greetings-circuit-breaker", "circuit-breaker")
      .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
      .doOnError(::onGreetingError).retry(5)
      .subscribe(::onGreetingReceived, ::onGreetingError, ::onGreetingComplete)
  }

  private fun circuitBreakerConfig() = CircuitBreakerConfig.custom()
    .failureRateThreshold(50f)
    .recordExceptions(WebClientResponseException.InternalServerError::class.java)
    .slidingWindowSize(5)
    .waitDurationInOpenState(1.seconds.toJavaDuration())
    .permittedNumberOfCallsInHalfOpenState(2)
    .build()

  private fun onGreetingReceived(message: String?): Unit =
    log.info("Received greeting: {}", message)

  private fun onGreetingError(ex: Throwable): Unit =
    when (ex) {
      is WebClientResponseException.InternalServerError ->
        log.error(
          "Oops! We got a {} from our network call. This will probably be a problem but we might try again... {}",
          ex.javaClass.simpleName,
          ex
        )

      is CallNotPermittedException ->
        log.error("Call not permitted error received while getting greeting: {}", ex.message, ex)

      else ->
        log.error("Unexpected error received while getting greeting: {}", ex.message, ex)
    }

  private fun onGreetingComplete(): Unit =
    log.info("Greeting with Circuit Breaker completed")
}
