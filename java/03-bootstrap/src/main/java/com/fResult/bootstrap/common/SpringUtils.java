package com.fResult.java_bootstrap.common;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

public abstract class SpringUtils {
  public static ConfigurableApplicationContext run(Class<?> componentClass, String profile) {
    final var applicationContext = new AnnotationConfigApplicationContext();

    if (StringUtils.hasText(profile)) {
      applicationContext.getEnvironment().setActiveProfiles(profile);
    }

    applicationContext.register(componentClass);
    applicationContext.refresh();

    applicationContext.start();
    return applicationContext;
  }
}
