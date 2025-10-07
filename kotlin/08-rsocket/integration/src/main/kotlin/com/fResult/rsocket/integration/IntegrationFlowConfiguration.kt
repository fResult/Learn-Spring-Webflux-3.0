package com.fResult.rsocket.integration

import com.fResult.rsocket.dtos.GreetingRequest
import com.fResult.rsocket.dtos.GreetingResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.transformer.FileToStringTransformer
import org.springframework.integration.rsocket.ClientRSocketConnector
import org.springframework.integration.rsocket.RSocketInteractionModel
import org.springframework.integration.rsocket.dsl.RSockets
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHeaders

@Configuration
class IntegrationFlowConfiguration {
  companion object {
    private val log: Logger = LogManager.getLogger(IntegrationFlowConfiguration::class.java)
  }

  @Bean
  fun greetingFlow(
    fileIn: FileReadingMessageSource,
    connector: ClientRSocketConnector,
    messageChannel: MessageChannel,
  ): IntegrationFlow = IntegrationFlow.from(fileIn)
    .transform(FileToStringTransformer())
    .transform(String::class.java) { GreetingRequest(it) }
    .handle(
      RSockets.outboundGateway("greetings")
        .interactionModel(RSocketInteractionModel.requestStream)
        .expectedResponseType(GreetingResponse::class.java)
        .clientRSocketConnector(connector)
    )
    .split()
    .channel(messageChannel)
    .handle(::logAndIgnoreGreetingResponse)
    .get()

  private fun logAndIgnoreGreetingResponse(payload: GreetingResponse, headers: MessageHeaders): Any? {
    log.info("-----------------")
    log.info(payload.toString())
    headers.forEach { key, value -> log.info("$key : $value") }

    return null
  }
}
