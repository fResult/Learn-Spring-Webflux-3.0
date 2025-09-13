package com.fResult.rsocket

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

class EncodingUtils(private val objectMapper: ObjectMapper) {
  private val objectReader = objectMapper.readerFor(Any::class.java)
  private val typeReference = typeRef<Map<String, Any>>()
}

inline fun <reified T> typeRef(): TypeReference<T> = object : TypeReference<T>() {}
