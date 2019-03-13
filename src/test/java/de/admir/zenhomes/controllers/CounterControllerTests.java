package de.admir.zenhomes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.admir.zenhomes.SpringBeans;
import de.admir.zenhomes.models.cfg.ServerCfg;
import de.admir.zenhomes.services.CounterService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CounterControllerTests {
	@InjectMocks
	private CounterController controller;
	@Spy
	private ServerCfg serverCfg = new ServerCfg(4321, "http://some-host-location.com");
	@Mock
	private CounterService counterService;

	private MockMvc mvc;

	private ObjectMapper objectMapper = SpringBeans.objectMapper();

	@Before
	public void initiTestee() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
}
