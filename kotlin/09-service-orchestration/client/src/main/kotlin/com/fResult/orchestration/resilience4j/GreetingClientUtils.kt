package com.fResult.orchestration.resilience4j

import com.fResult.orchestration.TimerUtils
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

object GreetingClientUtils {
  fun getGreetingFor(http: WebClient, clientUid: String, path: String): Mono<String?> {
    val parameterizeRegex = object : ParameterizedTypeReference<Map<String, String>>() {}
    val monoFromHttpCall = http.get()
      .uri("http://error-service/$path?uid=$clientUid")
      .retrieve()
      .bodyToMono(parameterizeRegex)
      .map { map -> map["greeting"] }

    return TimerUtils.monitor(monoFromHttpCall)
  }
}
