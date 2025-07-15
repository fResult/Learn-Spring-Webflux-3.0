package com.fResult.reactor.ch5_07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
class SchedulersExecutorServiceDecoratorsTest {
  private static final String RSB = "rsb";
  private final AtomicInteger methodInvocationCounter = new AtomicInteger(0);

  @BeforeEach
  void setUp() {
    Schedulers.resetFactory();
    Schedulers.addExecutorServiceDecorator(RSB, this::decorate);
  }

  @Test
  void changeDefaultDecorator() {
    val integers = Flux.just(1).delayElements(Duration.ofMillis(1));
    StepVerifier.create(integers)
        .thenAwait(Duration.ofMillis(10))
        .expectNextCount(1)
        .verifyComplete();
    assertEquals(1, methodInvocationCounter.get());
  }

  @AfterEach
  void tearDown() {
    Schedulers.resetFactory();
    Schedulers.removeExecutorServiceDecorator(RSB);
  }

  private ScheduledExecutorService decorate(
      Scheduler ignoredScheduler, ScheduledExecutorService executorService) {
    try {
      val pfb = new ProxyFactoryBean();
      pfb.setProxyInterfaces(new Class[] {ScheduledExecutorService.class});

      MethodInterceptor invoke = this::interceptAndCountScheduleMethods;
      pfb.addAdvice(invoke);
      pfb.setSingleton(true);
      pfb.setTarget(executorService);

      return (ScheduledExecutorService) pfb.getObject();
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    }
  }

  private Object interceptAndCountScheduleMethods(MethodInvocation methodInvocation) {
    val methodName = methodInvocation.getMethod().getName();
    Optional.of(methodName)
        .filter(this::startsWithSchedule)
        .ifPresent(
            name -> {
              methodInvocationCounter.incrementAndGet();
              log.info("methodName: [{}] incrementing", name);
            });

    try {
      return methodInvocation.proceed();
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    }
  }

  private boolean startsWithSchedule(String methodName) {
    return methodName.startsWith("schedule");
  }
}
