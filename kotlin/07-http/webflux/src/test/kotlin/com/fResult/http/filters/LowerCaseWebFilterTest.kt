package com.fResult.http.filters

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(LowerCaseWebConfiguration::class, LowerCaseWebFilter::class)
class LowerCaseWebFilterTest {
  @Autowired
  private lateinit var webClient: WebTestClient

  @Test
  fun greet() {
    test("/hi/jane", "Hello, jane!")
    test("/HI/jane", "Hello, jane!")
  }

  private fun test(path: String, match: String): Unit =
    webClient.get()
      .uri(path)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
      .expectBody(String::class.java)
      .value { message -> message.equals(match, ignoreCase = true) }
}
