package com.fresult.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class HttpSecurityConfiguration {
  @Bean
  fun authentication() =
    User.withUsername("MyUser").roles("USER").password("{noop}MyPassword").let { MapReactiveUserDetailsService(it.build()) }

  @Bean
  fun authorization(http: ServerHttpSecurity): SecurityWebFilterChain =
    http.authorizeExchange(::authorizeExchangeCustomizer)
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .httpBasic(Customizer.withDefaults())
      .build()

  private fun authorizeExchangeCustomizer(exchanges: ServerHttpSecurity.AuthorizeExchangeSpec) {
    exchanges.pathMatchers("/greet/authenticated").authenticated()
      .anyExchange().permitAll()
  }
}
