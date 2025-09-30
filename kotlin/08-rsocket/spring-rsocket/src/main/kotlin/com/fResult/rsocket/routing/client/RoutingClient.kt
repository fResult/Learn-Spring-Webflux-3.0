package com.fResult.rsocket.routing.client

import com.fResult.rsocket.routing.Customer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Controller

@Controller
class RoutingClient(private val requester: RSocketRequester) {
  companion object {
    private val log: Logger = LogManager.getLogger(RoutingClient::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    requester.route("customers")
      .retrieveFlux(Customer::class.java)
      .subscribe(
        ::onFetchCustomerSuccess,
        ::onFetchCustomerError,
        onFetchCustomerComplete(),
      )

    fetchCustomerById(1)
    fetchCustomerById(2)
  }

  private fun fetchCustomerById(id: Int) {
    requester.route("customers.{id}", id)
      .retrieveMono(Customer::class.java)
      .subscribe(
        ::onFetchCustomerSuccess,
        ::onFetchCustomerError,
        onFetchCustomerComplete(id),
      )
  }

  private fun onFetchCustomerSuccess(response: Customer) {
    log.info("Received customer: {}", response)
  }

  private fun onFetchCustomerError(ex: Throwable) {
    log.error("Failed to fetch customer: {}", ex.message, ex)
  }

  private fun onFetchCustomerComplete(id: Int? = null): () -> Unit = {
    id?.also { log.info("Completed fetching customer with id: {}", id) }
      ?: log.info("Completed fetching customers")
  }
}
