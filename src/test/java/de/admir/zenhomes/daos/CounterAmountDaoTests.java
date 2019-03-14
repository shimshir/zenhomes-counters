package de.admir.zenhomes.daos;

import de.admir.zenhomes.models.data.CounterAmount;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CounterAmountDaoTests {
	private CounterAmountDao counterAmountDao;

	@Before
	public void setUp() {
		counterAmountDao = new CounterAmountDao();
	}

	@Test
	public void insertCounterAmount_should_insert_amounts_of_one_counter_into_a_treemap_sorted_by_the_timestamp_desc() {
		CounterAmount counterAmount1 = new CounterAmount("1", 123.45, 1L);
		CounterAmount counterAmount2 = new CounterAmount("1", 234.56, 2L);
		CounterAmount counterAmount3 = new CounterAmount("1", 345.67, 3L);

		ArrayList<CounterAmount> randomOrderAmounts = new ArrayList<>();
		randomOrderAmounts.add(counterAmount1);
		randomOrderAmounts.add(counterAmount2);
		randomOrderAmounts.add(counterAmount3);
		Collections.shuffle(randomOrderAmounts);

		randomOrderAmounts.forEach(counterAmountDao::insertCounterAmount);

		List<CounterAmount> counterAmounts = new ArrayList<>(counterAmountDao.findByCounterId("1").get());

		assertEquals(counterAmount3, counterAmounts.get(0));
		assertEquals(counterAmount2, counterAmounts.get(1));
		assertEquals(counterAmount1, counterAmounts.get(2));

	}
}
