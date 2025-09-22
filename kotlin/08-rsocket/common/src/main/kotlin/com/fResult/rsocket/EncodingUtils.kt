package com.fResult.rsocket

import com.fResult.rsocket.fp.Functions.identity
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import kotlin.reflect.KClass

class EncodingUtils(private val objectMapper: ObjectMapper) {
  companion object {
    private val logger = LogManager.getLogger()
  }

  private val genericMapReference = typeRef<Map<String, Any>>()
  private val objectReader = objectMapper.readerFor(genericMapReference)

  fun <T : Any> decode(json: String, klass: KClass<T>): T =
    runCatching { objectMapper.readValue(json, klass.java) }
      .fold(
        onSuccess = ::identity,
        onFailure = jsonExceptionHandler("decode JSON into ${klass.simpleName}. JSON=${json.take(200)}"),
      )

  fun <T : Any> encode(obj: T): String =
    runCatching { objectMapper.writeValueAsString(obj) }
      .fold(
        onSuccess = ::identity,
        onFailure = jsonExceptionHandler("encode object of type ${obj::class.simpleName}"),
      )

  fun decodeMetadata(json: String): Map<String, Any> =
    runCatching { objectReader.readValue<Map<String, Any>>(json) }
      .fold(
        onSuccess = ::identity,
        onFailure = jsonExceptionHandler("decode metadata JSON. JSON=${json.take(200)}"),
      )

  fun encodeMetadata(metadata: Map<String, Any>): String =
    runCatching { objectMapper.writeValueAsString(metadata) }
      .fold(
        onSuccess = ::identity,
        onFailure = jsonExceptionHandler("encode metadata map"),
      )

  private inline fun <reified T> typeRef(): TypeReference<T> = object : TypeReference<T>() {}

  private fun <R> jsonExceptionHandler(context: String): (Throwable) -> R = { ex ->
    when (ex) {
      is JsonProcessingException -> {
        logger.error("JSON processing error during $context", ex)
        throw EncodingException("Unable to $context", ex)
      }

      else -> throw ex
    }
  }
}
