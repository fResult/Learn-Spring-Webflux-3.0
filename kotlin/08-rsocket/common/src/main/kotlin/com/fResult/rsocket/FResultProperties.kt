package com.fResult.rsocket

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fresult")
class FResultProperties {
  val rsocket: RSocket = RSocket()

  companion object {
    class RSocket(var hostname: String = "localhost", var port: Int = 8181)
  }
}
