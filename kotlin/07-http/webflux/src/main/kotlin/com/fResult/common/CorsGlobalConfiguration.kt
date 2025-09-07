package com.fResult.common

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsGlobalConfig : WebFluxConfigurer {
  override fun addCorsMappings(registry: CorsRegistry) {
    // Allow CORS for all paths and methods, for the MS Live Preview, ran by vscode
    registry.addMapping("/**")
      .allowedOrigins("http://localhost:3000")
      .allowedMethods("*")
      .allowedHeaders("*")
      .exposedHeaders("Content-Type", "Accept", "Cache-Control")
      .allowCredentials(true)
  }
}
