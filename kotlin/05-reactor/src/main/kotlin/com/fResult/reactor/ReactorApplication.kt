package com.fResult.reactor

import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints
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
  }
}
