package de.admir.zenhomes.services;

import de.admir.zenhomes.daos.CounterAmountDao;
import de.admir.zenhomes.daos.CounterDao;
import de.admir.zenhomes.models.data.Counter;
import de.admir.zenhomes.models.data.CounterAmount;
import de.admir.zenhomes.time.Chronos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CounterService {
	private final Chronos chronos;
	private final CounterDao counterDao;
	private final CounterAmountDao counterAmountDao;

	public String createCounter(Counter counter) {
		return counterDao.createCounter(counter);
	}

	public Optional<Counter> findCounterById(String id) {
		return counterDao.findCounterById(id);
	}

	public Optional<String> insertCounterAmount(String counterId, double amount) {
		return counterDao.findCounterById(counterId)
			.map(counter ->
				counterAmountDao.insertCounterAmount(
					new CounterAmount()
						.withCounterId(counterId)
						.withAmount(amount)
						.withTimestamp(chronos.epochMilli())
				)
			);
	}
}
