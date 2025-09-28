package com.fResult.rsocket.bidirectional.client

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.core.publisher.Flux

class BidirectionalClient(private val requester: RSocketRequester, private val uid: String) {
  companion object {
    val log: Logger = LogManager.getLogger(BidirectionalClient::class.java)
  }

  fun getGreetings(): Flux<GreetingResponse> =
    requester.route("greetings")
      .data(GreetingRequest("Client #$uid"))
      .retrieveFlux(GreetingResponse::class.java)
}
