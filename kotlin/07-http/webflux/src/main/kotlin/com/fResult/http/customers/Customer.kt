package com.fResult.http.customers

data class Customer(override val id: String?, val name: String) : EntityObject {
  constructor(name: String) : this(null, name)
}
