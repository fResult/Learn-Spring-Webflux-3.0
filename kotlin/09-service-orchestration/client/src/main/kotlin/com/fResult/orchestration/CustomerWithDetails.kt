package com.fResult.orchestration

data class CustomerWithDetails(val customer: Customer, val orders: List<Order>, val profile: Profile)
