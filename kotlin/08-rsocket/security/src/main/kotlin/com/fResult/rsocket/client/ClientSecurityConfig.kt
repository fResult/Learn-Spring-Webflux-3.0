package com.fResult.rsocket.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata

@Configuration
class ClientSecurityConfig {
  // Hardcoded users for demonstration purposes, don't do this in production
  @Bean
  fun credentials1(): UsernamePasswordMetadata = UsernamePasswordMetadata("fResult", "P@ssw0rd")

  @Bean
  fun credentials2(): UsernamePasswordMetadata = UsernamePasswordMetadata("KornZilla", "P@ssw0rd")
}
