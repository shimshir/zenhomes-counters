package de.admir.zenhomes.controllers;

import de.admir.zenhomes.models.data.Counter;
import de.admir.zenhomes.models.data.CounterAmount;
import de.admir.zenhomes.models.data.CounterReport;
import de.admir.zenhomes.services.CounterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConsumptionReportControllerTests {
	@InjectMocks
	private ConsumptionReportController controller;
	@Mock
	private CounterService counterService;

	private MockMvc mvc;

	@Before
	public void initTestee() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void GET_consumption_report_should_respond_with_an_empty_json_array_when_the_service_returns_an_empty_list() throws Exception {
		when(counterService.consumptionReport(24)).thenReturn(Collections.emptyList());

		mvc.perform(
			get("/consumption-report")
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json("[]"));
	}

	@Test
	public void GET_consumption_report_should_respond_with_a_json_array_of_elements_that_the_service_returns() throws Exception {
		long ts = Instant.now().toEpochMilli();
		CounterReport report = new CounterReport(
			new Counter("1", "Rodenbach"),
			new CounterAmount("1", 123.45, ts)
		);

		when(counterService.consumptionReport(24)).thenReturn(Collections.singletonList(report));

		mvc.perform(
			get("/consumption-report")
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json(
				String.format("[\n" +
					"  {\n" +
					"    \"counter\": {\n" +
					"      \"id\": \"1\",\n" +
					"      \"villageName\": \"Rodenbach\"\n" +
					"    },\n" +
					"    \"counterAmount\": {\n" +
					"      \"counterId\": \"1\",\n" +
					"      \"amount\": 123.45,\n" +
					"      \"timestamp\": %d,\n" +
					"      \"id\": \"1-%d\"\n" +
					"    }\n" +
					"  }\n" +
					"]", ts, ts)
			));
	}
}
