package de.admir.zenhomes.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class CounterAmount {
	@NotEmpty
	private String counterId;
	@NotNull
	@PositiveOrZero
	private double amount;
	private Long timestamp;

	public Optional<String> getId() {
		return getTimestamp().map(ts -> counterId + "-" + ts);
	}

	public Optional<Long> getTimestamp() {
		return Optional.ofNullable(timestamp);
	}

	@JsonIgnore
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
