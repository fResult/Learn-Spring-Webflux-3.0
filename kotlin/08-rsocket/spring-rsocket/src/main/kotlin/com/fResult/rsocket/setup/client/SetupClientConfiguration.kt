package com.fResult.rsocket.setup.client

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester

@Configuration
class SetupClientConfiguration(private val requester: RSocketRequester) {
  companion object {
    val log: Logger = LogManager.getLogger(SetupClientConfiguration::class.java)
  }

  @Bean
  fun applicationRunner(requester: RSocketRequester) = ApplicationRunner { args ->
    requester.route("greetings.{name}", "World")
      .retrieveMono(String::class.java)
      .subscribe(
        { response -> log.info("Response: {}", response) },
        { error -> log.error("Error: {}", error.message, error) },
      )
  }
}
