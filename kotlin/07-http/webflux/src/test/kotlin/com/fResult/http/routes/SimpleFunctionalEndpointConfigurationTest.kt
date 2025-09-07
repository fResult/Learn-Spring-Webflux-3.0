package com.fResult.http.routes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(SimpleFunctionalEndpointConfiguration::class, GreetingsHandlerFunction::class)
class SimpleFunctionalEndpointConfigurationTest {
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Test
  fun `hello john`(): Unit = parameterizeTestString("/hello/John", "Hello, John!")

  @Test
  fun hodor(): Unit = parameterizeTestString("/hodor", "Hodor!")

  @Test
  fun sup(): Unit = parameterizeTestString("/sup", "Hodor!")

  private fun parameterizeTestString(path: String, expectedResult: String) {
    webTestClient.get()
      .uri(path)
      .exchange()
      .expectBody(String::class.java)
      .value { str -> assertEquals(expectedResult, str) }
  }
}
