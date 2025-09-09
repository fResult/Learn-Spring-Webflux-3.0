package com.fresult.service

import com.fresult.client.Greeting
import org.reactivestreams.Publisher
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@RestController
@RequestMapping("/greet")
class HttpController {
  @GetMapping("/single/{name}")
  fun greetSingle(@PathVariable name: String): Publisher<Greeting> = Mono.just(greeting(name))

  @GetMapping("/many/{name}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun greetMany(@PathVariable name: String): Publisher<Greeting> =
    Flux.fromStream(Stream.generate { greeting(name) })
      .delayElements(1.seconds.toJavaDuration());

  private fun greeting(name: String) = Greeting("Hello, $name @ ${Instant.now()}")
}
