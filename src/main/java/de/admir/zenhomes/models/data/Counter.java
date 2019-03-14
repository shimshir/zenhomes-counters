package de.admir.zenhomes.models.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Data
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class Counter {
	private String id;
	@NotEmpty
	private String villageName;

	@JsonGetter
	public Optional<String> getId() {
		return Optional.ofNullable(id);
	}

	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}
}
