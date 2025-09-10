package com.fresult.client

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ClientRunner(private val defaultClient: DefaultClient, private val authenticatedClient: AuthenticatedClient) {
  companion object {
    private val log: Logger = LogManager.getLogger(ClientRunner::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    authenticatedClient.getAuthenticatedGreeting().map(::greetMessage).subscribe(loggingConsumer("Authenticated"))
    defaultClient.getSingle("KotlinSingle").map(::greetMessage).subscribe(loggingConsumer("Single"))
    defaultClient.getMany("KotlinMany").map(::greetMessage).subscribe(loggingConsumer("Many"))
  }

  private fun greetMessage(greeting: Greeting): String = greeting.message

  private fun loggingConsumer(tag: String): (String) -> Unit {
    return { message: String -> log.info("[$tag]: {}", message) }
  }
}
