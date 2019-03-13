package de.admir.zenhomes.daos;

import de.admir.zenhomes.models.data.CounterAmount;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CounterAmountDao {
	private final Map<String, Map<Long, CounterAmount>> counterAmounts = new ConcurrentHashMap<>();

	public String insertCounterAmount(CounterAmount counterAmount) {
		String counterId = counterAmount.getCounterId();
		long ts = counterAmount.getTimestamp().get();
		CounterAmount counterAmountWithTs = counterAmount.withTimestamp(ts);
		String generatedId = counterAmountWithTs.getId().get();

		counterAmounts.compute(counterId,
			(counterIdKey, sortedAmounts) -> {
				if (sortedAmounts == null) {
					Map<Long, CounterAmount> amounts =
						Collections.synchronizedMap(new TreeMap<Long, CounterAmount>(Comparator.reverseOrder()));

					amounts.put(ts, counterAmountWithTs);
					return amounts;
				} else {
					sortedAmounts.put(ts, counterAmountWithTs);
					return sortedAmounts;
				}
			});

		return generatedId;
	}
}
