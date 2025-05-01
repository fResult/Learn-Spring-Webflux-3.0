package com.fResult.bootstrap.customers.services

class DevelopmentOnlyCustomerServiceTest : BaseClass() {
  override fun getCustomerService(): CustomerService {
    return DevelopmentOnlyCustomerService()
  }
}
