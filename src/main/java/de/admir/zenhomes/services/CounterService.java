package de.admir.zenhomes.services;

import de.admir.zenhomes.daos.CounterAmountDao;
import de.admir.zenhomes.daos.CounterDao;
import de.admir.zenhomes.models.data.Counter;
import de.admir.zenhomes.models.data.CounterAmount;
import de.admir.zenhomes.models.data.CounterReport;
import de.admir.zenhomes.time.Chronos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CounterService {
	private final Chronos chronos;
	private final CounterDao counterDao;
	private final CounterAmountDao counterAmountDao;

	public String createCounter(Counter counter) {
		return counterDao.insertCounter(counter);
	}

	public Optional<Counter> findCounterById(String id) {
		return counterDao.findCounterById(id);
	}

	public Optional<String> insertCounterAmount(CounterAmount counterAmount) {
		return counterDao.findCounterById(counterAmount.getCounterId())
			.map(counter ->
				counterAmountDao.insertCounterAmount(
					counterAmount.withTimestamp(chronos.epochMilli())
				)
			);
	}

	public List<CounterReport> consumptionReport(long durationInHours) {
		long tsHoursAgo = chronos.epochMilli() - durationInHours * 60 * 60 * 1000;

		return counterDao.findAll().stream().flatMap(counter ->
			counterAmountDao.findByCounterId(counter.getId().get()).flatMap(counterAmounts ->
				counterAmounts.stream()
					.filter(counterAmount -> counterAmount.getTimestamp().get() >= tsHoursAgo)
					.findFirst()
			)
				.map(counterAmount -> new CounterReport(counter, counterAmount))
				.map(Stream::of).orElseGet(Stream::empty)
		).collect(Collectors.toList());
	}
}
