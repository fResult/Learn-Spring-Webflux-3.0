package com.fResult.orchestration.hedging.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("hedging.load-balancer")
data class HedgingLoadBalancerProperties(val maxNodes: Int = 3)
