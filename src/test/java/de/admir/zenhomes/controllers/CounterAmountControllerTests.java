package de.admir.zenhomes.controllers;

import de.admir.zenhomes.models.data.CounterAmount;
import de.admir.zenhomes.services.CounterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CounterAmountControllerTests {
	@InjectMocks
	private CounterAmountController controller;
	@Mock
	private CounterService counterService;

	private MockMvc mvc;

	@Before
	public void initTestee() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void PUT_counter_amounts_should_respond_with_no_content_when_the_counter_amount_gets_inserted_successfully() throws Exception {
		CounterAmount counterAmount = new CounterAmount().withCounterId("1").withAmount(123.45);

		when(counterService.insertCounterAmount(counterAmount)).thenReturn(Optional.of("1-1234567890"));

		mvc.perform(
			put("/counter-amounts")
				.content(
					"{\n" +
						"  \"counterId\": \"1\",\n" +
						"  \"amount\": 123.45\n" +
						"}"
				).contentType(MediaType.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isNoContent());
	}

	@Test
	public void PUT_counter_amounts_should_respond_with_not_found_when_the_counter_amount_can_not_be_inserted() throws Exception {
		CounterAmount counterAmount = new CounterAmount().withCounterId("1").withAmount(123.45);

		when(counterService.insertCounterAmount(counterAmount)).thenReturn(Optional.empty());

		mvc.perform(
			put("/counter-amounts")
				.content(
					"{\n" +
						"  \"counterId\": \"1\",\n" +
						"  \"amount\": 123.45\n" +
						"}"
				).contentType(MediaType.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isNotFound());
	}

	@Test
	public void PUT_counter_amounts_should_respond_with_bad_request_when_the_amount_is_negative() throws Exception {
		mvc.perform(
			put("/counter-amounts")
				.content(
					"{\n" +
						"  \"counterId\": \"1\",\n" +
						"  \"amount\": -123.45\n" +
						"}"
				).contentType(MediaType.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isBadRequest());

		verify(counterService, never()).insertCounterAmount(any());
	}

}
