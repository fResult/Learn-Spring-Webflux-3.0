package com.fResult.rsocket

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.logging.log4j.LogManager
import kotlin.reflect.KClass

class EncodingUtils(private val objectMapper: ObjectMapper) {
  companion object {
    private val logger = LogManager.getLogger()
  }

  private val typeReference = typeRef<Map<String, Any>>()
  private val objectReader = objectMapper.readerFor(typeReference)

  fun <T : Any> decode(json: String, klass: KClass<T>): T =
    runCatching { objectMapper.readValue(json, klass.java) }
      .getOrElse { ex ->
        when (ex) {
          is JsonProcessingException -> {
            logger.error("Failed to decode JSON into ${klass.simpleName}. JSON=${json.take(200)}", ex)
            throw EncodingException("Unable to encode ${klass.simpleName}", ex)
          }

          else -> throw ex
        }
      }

  fun <T : Any> encode(obj: T): String =
    runCatching { objectMapper.writeValueAsString(obj) }
      .getOrElse { ex ->
        when (ex) {
          is JsonProcessingException -> {
            logger.error("Failed to encode object of type ${obj::class.simpleName}", ex)
            throw EncodingException("Unable to encode ${obj::class.simpleName}", ex)
          }

          else -> throw ex
        }
      }



  private inline fun <reified T> typeRef(): TypeReference<T> = object : TypeReference<T>() {}
}
