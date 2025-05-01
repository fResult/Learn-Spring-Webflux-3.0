package com.fResult.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootstrapApplication {
	public static void main(String[] args) {
		final var customerService = new DevelopmentOnlyCustomerService();
		SpringApplication.run(BootstrapApplication.class, args);
	}
}
