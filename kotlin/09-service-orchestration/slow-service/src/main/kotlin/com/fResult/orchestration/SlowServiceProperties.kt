package com.fResult.orchestration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "slow-service")
data class SlowServiceProperties(var delayInSeconds: Int = 0)
