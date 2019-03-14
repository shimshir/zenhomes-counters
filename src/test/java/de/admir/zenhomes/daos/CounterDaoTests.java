package de.admir.zenhomes.daos;

import de.admir.zenhomes.models.data.Counter;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CounterDaoTests {
	private CounterDao counterDao;

	@Before
	public void setUp() {
		counterDao = new CounterDao();
	}

	@Test
	public void insertCounter_should_assign_an_id_and_store_the_counter() {
		Counter counter = new Counter().withVillageName("Rodenbach");

		String createdCounterId = counterDao.insertCounter(counter);

		assertEquals("1", createdCounterId);

		Optional<Counter> createdCounterOpt = counterDao.findCounterById(createdCounterId);

		assertTrue(createdCounterOpt.isPresent());
		assertEquals("1", createdCounterOpt.get().getId().get());
	}
}
