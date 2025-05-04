package com.fResult.bootstrap.common

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import kotlin.reflect.KClass

class SpringUtils {
  companion object {
    fun run(componentClass: KClass<*>, profile: String): ConfigurableApplicationContext {
      return AnnotationConfigApplicationContext().apply {
        profile.also { environment.setActiveProfiles(profile) }

        register(componentClass.java)
        refresh()
        start()
      }
    }
  }
}
