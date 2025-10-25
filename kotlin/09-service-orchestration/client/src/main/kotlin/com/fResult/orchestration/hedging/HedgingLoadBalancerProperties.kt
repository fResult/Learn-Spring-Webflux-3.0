package com.fResult.orchestration.hedging

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("hedging.load-balancer")
data class HedgingLoadBalancerProperties(val maxNodes: Int = 3)
