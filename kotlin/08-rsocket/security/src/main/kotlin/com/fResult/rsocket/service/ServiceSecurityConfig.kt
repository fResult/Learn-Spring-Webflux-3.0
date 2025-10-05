package com.fResult.rsocket.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor

@Configuration
class ServiceSecurityConfig {
  @Bean
  fun authentication(): MapReactiveUserDetailsService {
    // Note: the passwords are pre-encoded with BCryptPasswordEncoder (for `P@ssw0rd`) for demonstration purposes
    val user1 =
      buildUserDetailsEncodedPasswordByBCrypt("fResult", "d0VGi/3lKQQWNQz4aUUDNO1BfS6jr/9M3pRIRCCGL39SuJlNAh7zO", "ADMIN", "USER")
    val user2 = buildUserDetailsEncodedPasswordByBCrypt("KornZilla", "DdtIe6ShdBcjyvGD7rBJ8eTLWUqERjSGDGNSIyn7CXBeDL3znqxt.", "USER")

    return MapReactiveUserDetailsService(user1, user2)
  }

  @Bean
  fun authorization(security: RSocketSecurity): PayloadSocketAcceptorInterceptor =
    security.simpleAuthentication(Customizer.withDefaults()).build()

  @Bean
  fun rSocketMessageHandler(strategies: RSocketStrategies): RSocketMessageHandler =
    RSocketMessageHandler().apply {
      argumentResolverConfigurer.addCustomResolver(AuthenticationPrincipalArgumentResolver())
      rSocketStrategies = strategies
    }

  private fun buildUserDetailsEncodedPasswordByBCrypt(
    username: String,
    hashedPassword: String,
    vararg roles: String,
  ): UserDetails {
    println("Hashed Password: $hashedPassword")
    return User.withUsername(username)
      .password("{bcrypt}$2a$10$$hashedPassword")
      .roles(*roles)
      .build()
  }
}
