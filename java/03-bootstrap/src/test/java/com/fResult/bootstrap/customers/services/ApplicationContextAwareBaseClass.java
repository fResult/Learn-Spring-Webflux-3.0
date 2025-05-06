package com.fResult.bootstrap.customers.services;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class ApplicationContextAwareBaseClass extends BaseClass {
  private final AtomicReference<ConfigurableApplicationContext> reference = new AtomicReference<>();

  @Override
  public CustomerService getCustomerService() {
    return getCurrentContext().getBean(CustomerService.class);
  }

  protected ConfigurableApplicationContext getCurrentContext() {
    return reference.updateAndGet(
        context ->
            Optional.ofNullable(context).orElse(buildApplicationContext(getConfigurationClass())));
  }

  protected ConfigurableApplicationContext buildApplicationContext(
      Class<?> componentClass, String... profiles) {

    final var context = new AnnotationConfigApplicationContext();
    context.getEnvironment().setActiveProfiles(profiles);
    context.register(componentClass);
    context.refresh();

    return context;
  }

  protected abstract Class<?> getConfigurationClass();
}
