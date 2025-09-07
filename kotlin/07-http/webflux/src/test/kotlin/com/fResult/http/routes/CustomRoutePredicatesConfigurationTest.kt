package com.fResult.http.routes

import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test


@WebFluxTest(CustomRoutePredicatesConfiguration::class)
class CustomRoutePredicatesConfigurationTest {
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Test
  fun `test case insensitive request matching`() {
    // Given
    val upperCaseURI = "/GREETINGS/John"
    val lowerCaseURI = "/greetings/John"
    val expectedResult = "Hello, john!"

    // When
    val resultSpecForUpperCase = webTestClient.get().uri(upperCaseURI).exchange()
    val resultSpecForLowerCase = webTestClient.get().uri(lowerCaseURI).exchange()

    // Then
    resultSpecForUpperCase.expectBody(String::class.java).value { assertEquals(expectedResult, it) }
    resultSpecForLowerCase.expectBody(String::class.java).value { assertEquals(expectedResult, it) }
  }

  @Test
  fun `test case sensitive request matching`() {
    // Given
    val uriWithBadQueryParam = "/test?uid=4&name=John"
    val uri = "/test?uid=3&name=John"
    val expectedGoodResult = "Hello, John!"

    // When
    val resultSpecForUpperCase = webTestClient.get().uri(uriWithBadQueryParam).exchange()
    val resultSpecForLowerCase = webTestClient.get().uri(uri).exchange()

    // Then
    resultSpecForUpperCase.expectStatus().isNotFound()
    resultSpecForLowerCase.expectBody(String::class.java).value { assertEquals(expectedGoodResult, it) }
  }
}
