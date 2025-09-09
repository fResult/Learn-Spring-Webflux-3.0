package com.fresult.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(ClientProperties::class)
class HttpClientApplication

fun main(args: Array<String>) {
  SpringApplicationBuilder()
    .sources(HttpClientApplication::class.java)
    .profiles("client")
    .run(*args)

  Thread.sleep(20_000)
}
