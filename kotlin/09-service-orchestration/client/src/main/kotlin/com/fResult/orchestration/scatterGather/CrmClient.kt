package com.fResult.orchestration.scatterGather

import com.fResult.orchestration.Customer
import com.fResult.orchestration.Order
import com.fResult.orchestration.Profile
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class CrmClient(private val http: WebClient) {
  fun getCustomers(ids: Array<Int>): Flux<Customer> {
    val customersRoot = "http://customer-service/customers?ids=${StringUtils.arrayToDelimitedString(ids, ",")}"

    return http.get().uri(customersRoot).retrieve().bodyToFlux(Customer::class.java)
  }

  fun getCustomersOrders(customerIds: Array<Int>): Flux<Order> {
    val ordersRoot = "http://order-service/orders?customer-ids=${StringUtils.arrayToDelimitedString(customerIds, ",")}"

    return http.get().uri(ordersRoot).retrieve().bodyToFlux(Order::class.java)
  }

  /**
   * Simulates the N+1 problem described in Chapter 12 (Service Orchestration).
   *
   * While reactive WebClient enables *parallel execution* (avoiding serial latency),
   * this single-record fetch pattern remains a bad practice because:
   *
   * - Wastes network resources (N round-trips for N customers)
   * - Fails to leverage database batch query capabilities
   * - Creates unnecessary load on profile-service
   *
   * Prefer batch endpoints (e.g., GET `/profiles?customer-ids=1,2,3`) as demonstrated in getCustomers().
   *
   * See book section 12.7 - Reactive Scatter/Gather, Page 393 of 421:
   * "The getProfile method returns a single Profile, which is unfortunate..."
   */
  fun getCustomerProfile(customerId: Int): Mono<Profile> {
    val profilesRoot = "http://profile-service/profiles?customer-id=$customerId"

    return http.get().uri(profilesRoot).retrieve().bodyToMono(Profile::class.java)
  }
}
