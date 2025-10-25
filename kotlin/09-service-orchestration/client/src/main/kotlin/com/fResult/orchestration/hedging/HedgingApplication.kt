package com.fResult.orchestration.hedging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(HedgingLoadBalancerProperties::class)
class HedgingApplication

fun main(args: Array<String>) {
  runApplication<HedgingApplication>(*args)
}
