package com.fresult.client

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant

@Configuration
class RunnerConfiguration {
  companion object {
    private val log: Logger = LogManager.getLogger(RunnerConfiguration::class.java)
  }

  @Bean
  fun client(secureHttpClient: WebClient): ApplicationListener<ApplicationReadyEvent> =
    ApplicationListener { event ->
      secureHttpClient.get()
        .uri("http://localhost:8080/greetings")
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {})
        .subscribe(::logGreeting)
    }

  private fun logGreeting(map: Map<String, String>): Unit =
    log.info("Greeting: ${map["message"]} with roles: ${map["roles"]} @ ${Instant.now()}")
}
