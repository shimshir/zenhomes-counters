package de.admir.zenhomes.controllers;

import de.admir.zenhomes.models.cfg.ServerCfg;
import de.admir.zenhomes.models.data.Counter;
import de.admir.zenhomes.services.CounterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CounterControllerTests {
	@InjectMocks
	private CounterController controller;
	@Spy
	private ServerCfg serverCfg = new ServerCfg(4321, "http://some-host-location.com");
	@Mock
	private CounterService counterService;

	private MockMvc mvc;

	@Before
	public void initTestee() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void POST_counters_should_create_a_new_counter_and_return_its_location_in_a_location_header() throws Exception {
		String counterJson = "{\"villageName\": \"Rodenbach\"}";

		String idToGenerate = "1";

		when(counterService.createCounter(new Counter().withVillageName("Rodenbach"))).thenReturn(idToGenerate);

		mvc.perform(
			post("/counters")
				.content(counterJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", serverCfg.getLocation() + "/counters/" + idToGenerate));
	}

	@Test
	public void POST_counters_should_decline_a_counter_with_an_empty_villageName() throws Exception {
		String counterJson = "{\"villageName\": \"\"}";

		mvc.perform(
			post("/counters")
				.content(counterJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isBadRequest());

		verify(counterService, never()).createCounter(any());
	}

	@Test
	public void POST_counters_should_decline_a_counter_with_a_null_villageName() throws Exception {
		String counterJson = "{\"villageName\": null}";

		mvc.perform(
			post("/counters")
				.content(counterJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
		)
			.andExpect(status().isBadRequest());

		verify(counterService, never()).createCounter(any());
	}

	@Test
	public void GET_counters_id_should_respond_with_a_counter_json_body_when_the_counter_exists() throws Exception {
		String id = "1";
		Counter counter = new Counter(id, "Rodenbach");

		when(counterService.findCounterById(id)).thenReturn(Optional.of(counter));

		mvc.perform(
			get("/counters/" + id)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json("{\"id\": \"" + id + "\", \"villageName\": \"Rodenbach\"}"));
	}

	@Test
	public void GET_counters_id_should_respond_with_not_found_when_the_counter_does_not_exist() throws Exception {
		String id = "1";

		when(counterService.findCounterById(id)).thenReturn(Optional.empty());

		mvc.perform(
			get("/counters/" + id)
		)
			.andExpect(status().isNotFound())
			.andExpect(content().string(""));
	}
}
