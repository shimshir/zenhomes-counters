package de.admir.zenhomes.services;

import de.admir.zenhomes.daos.CounterAmountDao;
import de.admir.zenhomes.daos.CounterDao;
import de.admir.zenhomes.models.data.Counter;
import de.admir.zenhomes.models.data.CounterAmount;
import de.admir.zenhomes.models.data.CounterReport;
import de.admir.zenhomes.time.Chronos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CounterServiceTests {
	@InjectMocks
	private CounterService counterService;
	@Mock
	private Chronos chronos;
	@Mock
	private CounterDao counterDao;
	@Mock
	private CounterAmountDao counterAmountDao;

	@Test
	public void insertCounterAmount_should_insert_a_counter_amount_and_return_the_generated_id_when_the_counter_exists() {
		CounterAmount counterAmount =
			new CounterAmount()
				.withCounterId("1")
				.withAmount(123.45);

		long ts = Instant.now().toEpochMilli();

		when(counterDao.findCounterById(counterAmount.getCounterId())).thenReturn(Optional.of(mock(Counter.class)));
		when(chronos.epochMilli()).thenReturn(ts);
		when(counterAmountDao.insertCounterAmount(counterAmount.withTimestamp(ts)))
			.thenReturn(counterAmount.withTimestamp(ts).getId().get());

		String caId = counterService.insertCounterAmount(counterAmount).get();

		assertEquals(caId, counterAmount.getCounterId() + "-" + ts);
	}

	@Test
	public void insertCounterAmount_should_return_empty_when_the_counter_does_not_exists() {
		CounterAmount counterAmount =
			new CounterAmount()
				.withCounterId("1")
				.withAmount(123.45);

		when(counterDao.findCounterById(counterAmount.getCounterId())).thenReturn(Optional.empty());

		assertFalse(counterService.insertCounterAmount(counterAmount).isPresent());

		verifyZeroInteractions(chronos);
		verifyZeroInteractions(counterAmountDao);
	}

	@Test
	public void consumptionReport_should_return_a_report_when_there_is_a_counter_and_a_counter_amount_for_the_provided_duration() {
		long tsNow = Instant.now().toEpochMilli();
		Counter counter = new Counter("1", "Rodenbach");
		long ts1HourAgo = tsNow - 1000 * 60 * 60;
		CounterAmount counterAmount = new CounterAmount("1", 123.45, ts1HourAgo);

		when(chronos.epochMilli()).thenReturn(tsNow);
		when(counterDao.findAll()).thenReturn(Collections.singletonList(counter));
		when(counterAmountDao.findByCounterId(counter.getId().get()))
			.thenReturn(Optional.of(Collections.singletonList(counterAmount)));

		List<CounterReport> counterReports = counterService.consumptionReport(2);

		assertEquals(1, counterReports.size());

		CounterReport counterReport = counterReports.get(0);

		assertEquals(
			new CounterReport(
				counter,
				counterAmount
			),
			counterReport
		);

	}

	@Test
	public void consumptionReport_should_return_an_empty_report_when_there_are_no_counters() {
		long tsNow = Instant.now().toEpochMilli();

		when(chronos.epochMilli()).thenReturn(tsNow);
		when(counterDao.findAll()).thenReturn(Collections.emptyList());

		List<CounterReport> counterReports = counterService.consumptionReport(1);

		assertTrue(counterReports.isEmpty());
	}

	@Test
	public void consumptionReport_should_return_an_empty_report_when_there_is_a_counter_but_no_counter_amounts() {
		long tsNow = Instant.now().toEpochMilli();
		Counter counter = new Counter("1", "Rodenbach");

		when(chronos.epochMilli()).thenReturn(tsNow);
		when(counterDao.findAll()).thenReturn(Collections.singletonList(counter));
		when(counterAmountDao.findByCounterId(counter.getId().get()))
			.thenReturn(Optional.of(Collections.emptyList()));

		List<CounterReport> counterReports = counterService.consumptionReport(1);

		assertTrue(counterReports.isEmpty());
	}

	@Test
	public void consumptionReport_should_return_a_report_with_the_last_counter_amount_when_there_is_a_counter_and_many_counter_amounts_for_the_provided_duration() {
		long tsNow = Instant.now().toEpochMilli();
		Counter counter = new Counter("1", "Rodenbach");
		long ts1HourAgo = tsNow - 1000 * 60 * 60;
		long ts2HoursAgo = tsNow - 1000 * 60 * 60 * 2;
		CounterAmount counterAmount1 = new CounterAmount("1", 234.56, ts1HourAgo);
		CounterAmount counterAmount2 = new CounterAmount("1", 123.45, ts2HoursAgo);

		when(chronos.epochMilli()).thenReturn(tsNow);
		when(counterDao.findAll()).thenReturn(Collections.singletonList(counter));
		when(counterAmountDao.findByCounterId(counter.getId().get()))
			.thenReturn(Optional.of(Arrays.asList(counterAmount1, counterAmount2)));

		List<CounterReport> counterReports = counterService.consumptionReport(2);

		assertEquals(1, counterReports.size());

		CounterReport counterReport = counterReports.get(0);

		assertEquals(
			new CounterReport(
				counter,
				counterAmount1
			),
			counterReport
		);
	}
}
