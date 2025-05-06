package com.fResult.bootstrap;

import com.fResult.bootstrap.customers.services.ApplicationContextAwareBaseClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

class BootstrapApplicationTest extends ApplicationContextAwareBaseClass {
  private static final Logger LOG = LoggerFactory.getLogger(BootstrapApplicationTest.class);

  @Override
  protected ConfigurableApplicationContext buildApplicationContext(Class<?> componentClass, String... profiles) {
    LOG.info("Test for profiles: {}", (Object) profiles);

    return new SpringApplicationBuilder()
        .profiles(profiles)
        .sources(componentClass)
        .run();
  }

  @Override
  protected Class<BootstrapApplication> getConfigurationClass() {
    return BootstrapApplication.class;
  }
}
