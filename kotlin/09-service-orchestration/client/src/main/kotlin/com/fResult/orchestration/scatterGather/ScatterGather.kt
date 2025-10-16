package com.fResult.orchestration.scatterGather

import com.fResult.orchestration.TimerUtils
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.util.Logger
import reactor.util.Loggers

@Component
class ScatterGather(private val crmClient: CrmClient) {
  companion object {
    private val log: Logger = Loggers.getLogger(ScatterGather::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val ids = arrayOf(1, 2, 7, 5)
    TimerUtils.cache(crmClient.getCustomers(ids)).subscribe(
      { customer -> log.info("Received customer $customer") },
      { error -> log.error("Oops", error) },
      { log.info("Completed receiving all customers") },
    )
  }
}
