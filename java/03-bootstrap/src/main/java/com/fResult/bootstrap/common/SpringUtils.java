package com.fResult.bootstrap.common;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

public abstract class SpringUtils {
  private SpringUtils() {
    throw new IllegalStateException("This cannot be instantiated");
  }

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
