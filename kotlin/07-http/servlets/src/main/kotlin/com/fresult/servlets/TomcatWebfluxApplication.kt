package com.fresult.servlets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomcatWebfluxApplication

fun main(args: Array<String>) {
  runApplication<TomcatWebfluxApplication>(*args)
}
