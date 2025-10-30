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

  fun getOrders(customerIds: Array<Int>): Flux<Order> {
    val ordersRoot = "http://order-service/orders?customer-ids=${StringUtils.arrayToDelimitedString(customerIds, ",")}"

    return http.get().uri(ordersRoot).retrieve().bodyToFlux(Order::class.java)
  }

  fun getProfile(customerId: Int): Mono<Profile> {
    val profilesRoot = "http://profile-service/profiles?customer-id=$customerId"

    return http.get().uri(profilesRoot).retrieve().bodyToMono(Profile::class.java)
  }
}
