package com.fResult.orchestration.scatterGather

import com.fResult.orchestration.Customer
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class CrmClient(private val http: WebClient) {
  fun getCustomers(ids: Array<Int>): Flux<Customer> {
    val customersRoot = "http://customer-service/customers?ids=${StringUtils.arrayToDelimitedString(ids, ",")}"

    return http.get().uri(customersRoot).retrieve().bodyToFlux(Customer::class.java)
  }
}
