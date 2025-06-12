package com.fResult.reactor

import org.springframework.aop.SpringProxy
import org.springframework.aop.framework.Advised
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.DecoratingProxy
import org.springframework.scheduling.concurrent.ScheduledExecutorTask
import reactor.core.publisher.Flux

@SpringBootApplication
@ImportRuntimeHints(Hints::class)
class ReactorApplication

fun main(args: Array<String>) {
  runApplication<ReactorApplication>(*args)
}

class Hints : RuntimeHintsRegistrar {
  override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
    Flux.just(1)
    val prefix = "reactor.core.publisher.Traces"
    val classes = listOf(
      prefix,
      "$prefix\$StackWalkerCallSiteSupplierFactory",
      "$prefix\$SharedSecretsCallSiteSupplierFactory",
      "$prefix\$ExceptionCallSiteSupplierFactory"
    )

    classes.forEach { hints.reflection().registerType(TypeReference.of(it), *MemberCategory.entries.toTypedArray()) }

    hints.proxies()
      .registerJdkProxy(
        ScheduledExecutorTask::class.java,
        SpringProxy::class.java,
        Advised::class.java,
        DecoratingProxy::class.java
      )
  }
}
