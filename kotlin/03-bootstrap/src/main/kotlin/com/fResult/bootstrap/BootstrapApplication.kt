package com.fResult.bootstrap

import com.fResult.bootstrap.customers.services.DevelopmentOnlyCustomerService
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BootstrapApplication

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
	val customerService = DevelopmentOnlyCustomerService()

	Demo.workWithCustomerService(BootstrapApplication::class, customerService)
}
