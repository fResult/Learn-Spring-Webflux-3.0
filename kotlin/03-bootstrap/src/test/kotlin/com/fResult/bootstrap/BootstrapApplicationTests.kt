package com.fResult.bootstrap

import com.fResult.bootstrap.customers.services.ApplicationContextAwareBaseClass
import org.apache.logging.log4j.LogManager
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import kotlin.reflect.KClass

class BootstrapApplicationTests : ApplicationContextAwareBaseClass() {
  private val log = LogManager.getLogger(BootstrapApplicationTests::class)

  override fun buildApplicationContext(
    componentClass: KClass<*>,
    vararg profiles: String,
  ): ConfigurableApplicationContext {
    log.info("Test for profiles: {}", profiles)

    return SpringApplicationBuilder()
      .profiles(*profiles)
      .sources(componentClass.java)
      .run()
  }

  override fun getConfigurationClass(): KClass<BootstrapApplication> = BootstrapApplication::class
}
