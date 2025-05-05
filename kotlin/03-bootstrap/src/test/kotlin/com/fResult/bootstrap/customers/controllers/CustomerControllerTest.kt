package com.fResult.bootstrap.customers.controllers

import com.fResult.bootstrap.customers.Customer
import com.fResult.bootstrap.customers.services.CustomerService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@WebMvcTest(CustomerController::class)
class CustomerControllerTest {
  private val path = "/customers"
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var webApplicationContext: WebApplicationContext

  @MockitoBean
  private lateinit var customerService: CustomerService

  @BeforeEach
  fun setUp(): Unit {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
  }

  @Test
  fun `fetch all customers then success`() {
    // Given
    given(customerService.findAll()).willReturn(Collections.singletonList(Customer(1, "Wick")))

    // When
    val resultActions = mockMvc.perform(get(path))

    // Then
    resultActions.run {
      andExpectAll(
        status().isOk(),
        content().contentType(MediaType.APPLICATION_JSON),
        jsonPath("@.[0].name").value("Wick")
      )
    }
  }

  @Test
  fun `fetch customer by id then success`() {
    // Given
    val targetId = 1L
    val expectedCustomer = Customer(targetId, "Anderson")
    given(customerService.findById(targetId)).willReturn(expectedCustomer)

    // When
    val resultActions = mockMvc.perform(get("$path/{id}", targetId))

    // Then
    resultActions.run {
      andExpectAll(
        status().isOk(),
        content().contentType(MediaType.APPLICATION_JSON),
        jsonPath("@.id").value(expectedCustomer.id),
        jsonPath("@.name").value(expectedCustomer.name),
      )
    }
  }

  @Test
  fun `create customer then success`() {
    // Given
    val expectedCustomer = Customer(42L, "Wick")
    val body = "{\"name\": \"${expectedCustomer.name}\"}"
    given(customerService.save(expectedCustomer.name)).willReturn(listOf(expectedCustomer))

    // When
    val resultAction = mockMvc.perform(
      post(path)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
    )

    resultAction.run {
      andExpectAll(
        status().isOk(),
        content().contentType(MediaType.APPLICATION_JSON),
        jsonPath("@.[0].id").value(expectedCustomer.id),
        jsonPath("@.[0].name").value(expectedCustomer.name),
      )
    }
  }
}
