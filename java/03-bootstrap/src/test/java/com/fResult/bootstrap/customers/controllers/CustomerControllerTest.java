package com.fResult.bootstrap.customers.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fResult.bootstrap.customers.Customer;
import com.fResult.bootstrap.customers.services.CustomerService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
  private static final String PATH = "/customers";

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext webApplicationContext;

  @MockitoBean private CustomerService customerService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void all() throws Exception {
    // Given
    given(customerService.findAll())
        .willReturn(Collections.singletonList(new Customer(1L, "Wick")));

    // When
    final var resultActions = mockMvc.perform(get(PATH));

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name").value("Wick"));
  }

  @Test
  void byId() throws Exception {
    // Given
    final long targetId = 1;
    final var expectedCustomer = new Customer(targetId, "Anderson");
    given(customerService.findById(targetId)).willReturn(expectedCustomer);

    // When
    final var resultActions = mockMvc.perform(get(PATH + "/{id}", targetId));

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("@.id").value(expectedCustomer.id()))
        .andExpect(jsonPath("@.name").value(expectedCustomer.name()));
  }

  @Test
  void create() throws Exception {
    // Given
    final var expectedCustomer = new Customer(42L, "Wick");
    final var body = "{\"name\": \"" + expectedCustomer.name() + "\"}";
    given(customerService.save(expectedCustomer.name())).willReturn(List.of(expectedCustomer));

    // When
    final var resultActions =
        mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON).content(body));

    // Then
    resultActions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value(expectedCustomer.id()))
        .andExpect(jsonPath("$[0].name").value(expectedCustomer.name()));
  }
}
