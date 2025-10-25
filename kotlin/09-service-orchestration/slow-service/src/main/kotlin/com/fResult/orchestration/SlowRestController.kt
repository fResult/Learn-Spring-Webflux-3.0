package com.fResult.orchestration

import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.event.EventListener
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.util.Loggers
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaDuration

@RestController
class SlowRestController(private val props: SlowServiceProperties) {
  private val port = AtomicInteger()
  private val delay = props.delayInSeconds

  companion object {
    private val log = Loggers.getLogger(SlowRestController::class.java)
  }

  @EventListener
  fun onWebServerInitialized(event: WebServerInitializedEvent) {
    port.set(event.webServer.port)

    log.info("Slow service is running on port ${event.webServer.port}, delay is $delay seconds")

    if (log.isInfoEnabled) {
      log.info("Configured `slow-service.delay` is $delay ms on port ${port.get()}")
    }
  }

  @OptIn(ExperimentalTime::class)
  @GetMapping("/greetings")
  fun greet(@RequestParam(required = false, defaultValue = "World") name: String): Mono<GreetingResponse> {
    val startedAt = Clock.System.now()
    log.info("Received greeting request for name='$name' on port ${port.get()} at $startedAt, will delay for $delay seconds")

    return Mono.just(GreetingResponse("Hello, $name! (from port ${port.get()} started at $startedAt and finished at ${Clock.System.now()})"))
      .doOnNext { response -> log.info("Greeting response: $response") }
      .delaySubscription(delay.seconds.toJavaDuration())
  }
}
