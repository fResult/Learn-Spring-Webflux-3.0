package com.fResult.http.filters

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.UUID
import kotlin.reflect.jvm.jvmName

@Configuration
class LowerCaseWebConfiguration {
  companion object {
    val log: Logger = LogManager.getLogger(LowerCaseWebConfiguration::class.java)
  }

  @Bean
  fun routerFunctionFilters(): RouterFunction<ServerResponse> =
    UUID::class.jvmName.let { uuidKey ->
      RouterFunctions.route()
        .GET("/hi/{name}", ::handle)
        .GET("/hello/{name}", ::handle)
        .filter { req, next ->
          log.info("<1> .filter(): before")
          next.handle(req).also { log.info("<4> .filter(): after") }
        }
        .before { request ->
          request.also { log.info("<2> .before") }
            .apply { attributes().put(uuidKey, UUID.randomUUID()) }
        }
        .after { request, response ->
          request.also {
            log.info("<4> .after()")
            log.info("UUID: {}", request.attributes().getOrElse(uuidKey) { "not found" })
          }.let { response }
        }
        .onError(NullPointerException::class.java) { ex, request ->
          ServerResponse.badRequest().build()
        }.build()
    }

  fun handle(request: ServerRequest): Mono<ServerResponse> =
    ServerResponse.ok().bodyValue("Hello, ${request.pathVariable("name")}!")
}
