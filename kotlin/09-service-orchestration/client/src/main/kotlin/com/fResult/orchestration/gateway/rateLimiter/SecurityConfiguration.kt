package com.fResult.orchestration.gateway.rateLimiter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

/**
 * From 12.8.3
 *
 * - [Reactive Spring Book GitHub's RateLimiterConfiguration](https://github.com/reactive-spring-book/orchestration/blob/main/client/src/main/java/rsb/orchestration/gateway/SecurityConfiguration.java)
 */
@Configuration
class SecurityConfiguration {
  companion object {
    // Note: the passwords are pre-encoded with BCryptPasswordEncoder (for `P@ssw0rd`) for demonstration purposes
    const val HASHED_PASSWORD = "d0VGi/3lKQQWNQz4aUUDNO1BfS6jr/9M3pRIRCCGL39SuJlNAh7zO"
  }

  @Bean
  fun authorization(http: ServerHttpSecurity): SecurityWebFilterChain =
    http.httpBasic { _ -> Customizer.withDefaults<ServerHttpSecurity.HttpBasicSpec>() }
      .csrf { spec -> spec.disable() }
      .authorizeExchange { exchange ->
        exchange.pathMatchers("/rate-limiter", "/error-service/**", "/customer-service/**").authenticated()
          .anyExchange().permitAll()
      }.build()

  @Bean
  fun authentication(): MapReactiveUserDetailsService =
    User.withUsername("fResult").password("{bcrypt}$2a$10$$HASHED_PASSWORD").roles("USER").build()
      .let { MapReactiveUserDetailsService(it) }
}
