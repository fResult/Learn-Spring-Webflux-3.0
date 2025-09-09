package com.fresult.service

import com.fresult.client.ClientProperties
import com.fresult.client.DefaultClient
import com.fresult.client.DefaultConfiguration
import com.fresult.client.Greeting
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest(
  classes = [DefaultConfiguration::class, ClientProperties::class, HttpServiceApplication::class],
  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
  properties = [
    "spring.profiles.active=client",
    "server.port=8080",
    "spring.main.web-application-type=reactive",
  ]
)
class HttpControllerTest {
  @Autowired
  private lateinit var defaultClient: DefaultClient

  @Test
  fun greetSingle() {
    val name = "Madhura"
    defaultClient.getSingle(name).also(verifyGreeting(name))
  }

  @Test
  fun greetMany() {
    val name = "Stephane"
    val greetings = defaultClient.getMany(name).take(2)
    verifyGreetings(greetings, name)
  }

  private fun verifyGreeting(name: String): (Mono<Greeting>) -> Unit = { greeting ->
    StepVerifier
      .create(greeting)
      .expectNextMatches(containsHello(name))
      .verifyComplete()
  }

  private fun verifyGreetings(greeting: Flux<Greeting>, name: String): Unit {
    StepVerifier
      .create(greeting)
      .expectNextMatches(containsHello(name))
      .expectNextMatches(containsHello(name))
      .verifyComplete()
  }

  private fun containsHello(name: String): (Greeting) -> Boolean {
    return { g: Greeting ->
      g.message.contains("Hello, $name")
    }
  }
}
