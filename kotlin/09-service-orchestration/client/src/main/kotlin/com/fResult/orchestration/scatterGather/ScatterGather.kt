package com.fResult.orchestration.scatterGather

import com.fResult.orchestration.Customer
import com.fResult.orchestration.CustomerWithDetails
import com.fResult.orchestration.Order
import com.fResult.orchestration.TimerUtils
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3
import reactor.util.Logger
import reactor.util.Loggers

@Component
class ScatterGather(private val crmClient: CrmClient) {
  companion object {
    private val log: Logger = Loggers.getLogger(ScatterGather::class.java)
  }

  @EventListener(ApplicationReadyEvent::class)
  fun onApplicationReady() {
    val customerIds = arrayOf(1, 2, 7, 5)
    val customers = TimerUtils.cache(crmClient.getCustomers(customerIds))
    val orders = TimerUtils.cache(crmClient.getCustomersOrders(customerIds))
    val customerWithDetails = customers.flatMap(enrichWithOrdersAndProfile(orders))

    (1..5).forEach(monitorAndLogCustomerOrders(customerWithDetails))
  }

  private fun enrichWithOrdersAndProfile(orders: Flux<Order>): (Customer) -> Flux<CustomerWithDetails> = { customer ->
    val orderFor = ::isOrderForCustomer

    Flux.zip(
      customer.toMono(),
      orders.filter(orderFor(customer.id)).collectList(),
      crmClient.getCustomerProfile(customer.id)
    ).map { (customer, orderList, profile) -> CustomerWithDetails(customer, orderList, profile) }
  }

  private fun isOrderForCustomer(customerId: Int): (Order) -> Boolean = { order ->
    order.customerId == customerId
  }

  private fun monitorAndLogCustomerOrders(details: Flux<CustomerWithDetails>): (Int) -> Unit = { _ ->
    TimerUtils.monitor(details).subscribe(::logCustomerDetails)
  }

  private fun logCustomerDetails(details: CustomerWithDetails) {
    log.info("---------------")
    log.info("Customer: ${details.customer}")
    log.info("Profile: ${details.profile}")
    details.orders.forEach { order -> log.info("Order: {}", order) }
  }
}
