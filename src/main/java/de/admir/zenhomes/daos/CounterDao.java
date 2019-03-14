package de.admir.zenhomes.daos;

import de.admir.zenhomes.models.data.Counter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CounterDao {
	private final Map<String, Counter> counters = new ConcurrentHashMap<>();
	private final AtomicInteger idGenerator = new AtomicInteger(0);

	public String createCounter(Counter counter) {
		String id = String.valueOf(idGenerator.incrementAndGet());
		counters.put(id, counter.withId(id));

		return id;
	}

	public Optional<Counter> findCounterById(String id) {
		return Optional.ofNullable(counters.get(id));
	}

	public Collection<Counter> findAll() {
		return counters.values();
	}
}
