package com.fresult.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfiguration {
  @Bean
  fun authentication(): MapReactiveUserDetailsService {
    val user1 = buildUserDetails("fResult", "password1", "USER")
    val user2 = buildUserDetails("KornZilla", "password2", "USER", "ADMIN")

    return MapReactiveUserDetailsService(user1, user2)
  }

  @Bean
  fun authorization(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
    return httpSecurity
      .httpBasic(Customizer.withDefaults())
      .authorizeExchange(::customAuthorizeExchange)
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .build()
  }

  private fun buildUserDetails(
    username: String,
    password: String,
    vararg roles: String,
  ): UserDetails =
    User.withUsername(username)
      .password("{noop}$password")
      .roles(*roles)
      .build()

  private fun customAuthorizeExchange(
    authorizeExchange: ServerHttpSecurity.AuthorizeExchangeSpec,
  ): ServerHttpSecurity.AuthorizeExchangeSpec =
    authorizeExchange
      .pathMatchers("/greetings").authenticated()
      .anyExchange().permitAll()
}
