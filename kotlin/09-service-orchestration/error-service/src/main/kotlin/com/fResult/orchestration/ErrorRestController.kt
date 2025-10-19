package com.fResult.orchestration

import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.event.EventListener
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.util.Loggers
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@RestController
class ErrorRestController {
  companion object {
    const val UID_PARAM_NAME = "uid"
    private val log = Loggers.getLogger(ErrorRestController::class.java)
  }

  private val port = AtomicInteger()
  private val clientCounts = ConcurrentHashMap<String, AtomicInteger>()

  @EventListener
  fun onWebServerInitialized(event: WebServerInitializedEvent) {
    log.info("Error service is running on port ${event.webServer.port}")

    port.set(event.webServer.port)
  }

  @GetMapping("/ok")
  fun okEndpoint(@RequestParam(required = false, name = UID_PARAM_NAME) clientId: String?): Mono<GreetingResponse> {
    val countThusFar = registerAndCountClient(clientId)
    return greetingResponse(countThusFar, port.get())
  }

  @GetMapping("/retry")
  fun retryEndpoint(@RequestParam(required = false, name = UID_PARAM_NAME) clientId: String?): Mono<GreetingResponse> {
    val countThusFar = registerAndCountClient(clientId)
    log.info("Retry endpoint called for clientId=$clientId, attempt #$countThusFar")

    return if (countThusFar > 2) greetingResponse(countThusFar, port.get())
    else Mono.error(IllegalArgumentException())
  }

  @GetMapping("/circuit-breaker")
  fun circuitBreakerEndpoint(
    @RequestParam(
      required = false,
      name = UID_PARAM_NAME
    ) clientId: String?,
  ): Mono<Map<String, String>> {
    registerAndCountClient(clientId)

    return Mono.error(IllegalArgumentException())
  }

  private fun registerAndCountClient(clientId: String?): Int {
    return clientId?.let {
      clientCounts.computeIfAbsent(it) { AtomicInteger() }
        .incrementAndGet()
    } ?: 1
  }

  private fun greetingResponse(count: Int, port: Int): Mono<GreetingResponse> =
    Mono.just(GreetingResponse("Greeting attempt #$count from port $port"))
}
