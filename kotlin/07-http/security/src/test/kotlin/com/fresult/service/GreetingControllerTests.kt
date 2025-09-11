package com.fresult.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.test.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  properties = [
    "spring.profiles.active=client",
    "spring.main.web-application-type=reactive",
  ]
)
class GreetingControllerTests {
  @LocalServerPort
  private lateinit var port: Integer

  @Test
  fun greetAuthenticated() {
    val user = "fResult"
    val password = "password1"
    ExchangeFilterFunctions.basicAuthentication(user, password)
      .let { WebClient.builder().filter(it).build() }
      .let {
        it.get()
          .uri("http://localhost:$port/greetings")
          .retrieve()
          .bodyToMono(object : ParameterizedTypeReference<Map<String, String>>() {})
          .let {
            StepVerifier.create(it)
              .expectNextMatches { response ->
                response["message"]?.contains("fResult") == true
                    && response["roles"]?.contains("USER") == true
              }.verifyComplete()
          }
      }
  }

}
