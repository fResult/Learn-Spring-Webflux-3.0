package com.fresult.client

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ClientRunner(private val client: DefaultClient) {
  companion object {
    private val log: Logger = LogManager.getLogger(ClientRunner::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun runAfterStartup() {

    client.getSingle("KotlinSingle").map(::greetMessage).subscribe { message ->
      log.info("[Single]: {}", message)
    }

    client.getMany("KotlinMany").map(::greetMessage).subscribe { message ->
      log.info("[Many]: {}", message)
    }
  }

  private fun greetMessage(greeting: Greeting): String = greeting.message
}
