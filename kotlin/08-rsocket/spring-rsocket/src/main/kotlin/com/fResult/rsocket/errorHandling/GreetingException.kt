package com.fResult.rsocket.errorHandling

class GreetingException : RuntimeException{
  constructor(message: String, cause: Exception) : super(message, cause)
  constructor(message: String) : super(message)
  constructor(cause: Exception) : super(cause)
}
