package com.fResult.bootstrap.customers.services

import com.fResult.bootstrap.BootstrapApplication
import kotlin.reflect.KClass

class ComponentScannedCustomerServiceTest : ApplicationContextAwareBaseClass() {
  override fun getConfigurationClass(): KClass<BootstrapApplication> = BootstrapApplication::class
}
