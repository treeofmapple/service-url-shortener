package com.tom.service.shortener;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.service.shortener.request.URLRequest;
import com.tom.service.shortener.request.URLShortRequest;
import com.tom.service.shortener.service.URLService;

import net.datafaker.Faker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "admin")
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
class ShortURLIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private URLService service;
	
	private URLRequest urlRequest;
	private URLShortRequest shortRequest;
	
	private final Faker faker = new Faker();
	
	private final String request = faker.internet().url(); 
	
	@BeforeEach
	void setUp() {
		shortRequest = new URLShortRequest(request);
	}
	
	@Test
	@Order(1)
	@DisplayName("1 - Shorten URL")
	void shortenURLTest() throws Exception {
		urlRequest = new URLRequest(request);
		mockMvc.perform(post("/api/v1/shorten")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(urlRequest)))
	            .andExpect(status().isCreated());
		
	}
	
	@Test
	@Order(2)
	@DisplayName("2 - Find Basic URL")
	void findBasicURL() throws Exception {
		mockMvc.perform(get("/api/v1/basic")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(shortRequest)))
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(3)
	@DisplayName("3 - Find All URL")
	void findAllURL() throws Exception {
		mockMvc.perform(get("/api/v1/dev/get")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(shortRequest)))
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(4)
	@DisplayName("4 - Find Object URL")
	void findObjectURL() throws Exception {
		mockMvc.perform(get("/api/v1/dev/object")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(shortRequest)))
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(5)
	@DisplayName("5 - Find Full URL")
	void findFullURL() throws Exception {
		mockMvc.perform(get("/api/v1/dev/full")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(shortRequest)))
				.andExpect(status().isOk());
	}

	@Test
	@Order(6)
	@DisplayName("6 - Redirect URL")
	void redirectUrl() throws Exception {
		var url = service.findBasicURL(shortRequest);
		mockMvc.perform(get("/" + url.shortUrl()))
				.andExpect(status().isFound());
	}
	
	@Test
	@Order(7)
	@DisplayName("7 - Delete URL")
	void deleteURL() throws Exception {
		mockMvc.perform(delete("/api/v1/dev/delete")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(objectMapper.writeValueAsString(shortRequest)))
		        .andExpect(status().isNoContent());

	}
	
	@Test
	@Order(8)
	@DisplayName("8 - Actuator Health")
	void checkActuator() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk());

	}
	
}