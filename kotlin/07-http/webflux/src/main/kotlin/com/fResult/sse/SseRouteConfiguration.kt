package com.fResult.sse

import com.fResult.utils.IntervalMessageProducer
import kotlinx.coroutines.reactive.asFlow
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SseRouteConfiguration {
  companion object {
    private const val COUNT_PATH_VARIABLE: String = "count"
    private val log: Logger = LogManager.getLogger(SseRouteConfiguration::class.java)
  }

  @Bean
  fun routes(): RouterFunction<ServerResponse> = coRouter {
    GET("/sse/{$COUNT_PATH_VARIABLE}", ::handleSse)
  }

  private suspend fun handleSse(request: ServerRequest): ServerResponse {
    val count = request.pathVariable(COUNT_PATH_VARIABLE).toInt()
    val publisher = IntervalMessageProducer.produce(count).doOnComplete {log.info("Completed SSE stream for count: $count")}

    return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).bodyAndAwait(publisher.asFlow())
  }
}
