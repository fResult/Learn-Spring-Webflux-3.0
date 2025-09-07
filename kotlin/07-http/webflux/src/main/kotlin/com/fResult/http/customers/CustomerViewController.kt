package com.fResult.http.customers

import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CustomerViewController(private val repository: CustomerRepository) {
  @GetMapping("/c/customers.php")
  fun customersView(model: Model): String {
    val modelMap = mapOf(
      "customers" to repository.findAll().asFlow(),
      "type" to "@Controller",
    )
    model.addAllAttributes(modelMap)

    return "customers"
  }
}
