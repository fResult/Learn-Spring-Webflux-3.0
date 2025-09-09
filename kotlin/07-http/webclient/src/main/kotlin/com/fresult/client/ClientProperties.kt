package com.fresult.client

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "client")
class ClientProperties(val http: Http = Http()) {
  class Http(
    val rootUrl: String = "http://localhost:8080",
    val basic: Basic = Basic(),
  ) {
    class Basic(
      var username: String? = null,
      var password: String? = null,
    )
  }
}
