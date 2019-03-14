package de.admir.zenhomes.models.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class CounterReport {
	private Counter counter;
	private CounterAmount counterAmount;
}
