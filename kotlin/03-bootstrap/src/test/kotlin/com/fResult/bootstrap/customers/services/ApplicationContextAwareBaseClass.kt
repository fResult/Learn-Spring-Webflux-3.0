package com.fResult.bootstrap.customers.services

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

abstract class ApplicationContextAwareBaseClass : BaseClass() {
  private val reference: AtomicReference<ConfigurableApplicationContext> = AtomicReference()

  override fun getCustomerService(): CustomerService {
    return getCurrentContext().getBean(CustomerService::class.java)
  }

  private fun getCurrentContext(): ConfigurableApplicationContext {
    return reference.updateAndGet { buildApplicationContext(getConfigurationClass()) }
  }

  protected abstract fun getConfigurationClass(): KClass<*>

  private fun buildApplicationContext(
    componentClass: KClass<*>,
    vararg profiles: String,
  ): ConfigurableApplicationContext = AnnotationConfigApplicationContext().apply {
    environment.setActiveProfiles(*profiles)
    register(componentClass.java)
    refresh()
  }
}
