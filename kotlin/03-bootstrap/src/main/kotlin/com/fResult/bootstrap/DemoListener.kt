package com.fResult.bootstrap

import com.fResult.bootstrap.customers.services.CustomerService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class DemoListener(val customerService: CustomerService) {
  @EventListener(ApplicationReadyEvent::class)
  fun exercise() = Demo.workWithCustomerService(BootstrapApplication::class, customerService)
}
