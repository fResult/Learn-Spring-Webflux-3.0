package com.fResult.bootstrap.common.utils

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import kotlin.reflect.KClass

class SpringUtils {
  companion object {
    fun run(componentClass: KClass<*>, profile: String): ConfigurableApplicationContext {
      return AnnotationConfigApplicationContext().apply {
        profile.takeIf(String::isNotBlank)?.also { environment.setActiveProfiles(it) }

        register(componentClass.java)
        refresh()
        start()
      }
    }
  }
}
