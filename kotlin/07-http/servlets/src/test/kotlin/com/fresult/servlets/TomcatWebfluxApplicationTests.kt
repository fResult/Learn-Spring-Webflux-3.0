package com.fresult.servlets

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest
@Import(GreetingsRouteConfiguration::class)
class TomcatWebfluxApplicationTests {
  @Autowired
  private lateinit var webTestClient: WebTestClient

  @ParameterizedTest
  @CsvSource(*["functional:John", "controller:Jane"], delimiter = ':')
  fun controller(from: String, name: String) {
    webTestClient.get()
      .uri("/hello/$from/{name}", name)
      .exchange()
      .expectStatus()
      .isOk()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody(Greetings::class.java)
      .value { greetings -> Assertions.assertEquals(greetings?.message, "Hello, $name from $from!") }
  }
}
