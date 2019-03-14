package de.admir.zenhomes.controllers;

import de.admir.zenhomes.models.data.CounterAmount;
import de.admir.zenhomes.services.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/counter-amounts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CounterAmountController {
	private final CounterService counterService;

	@PutMapping
	public ResponseEntity postCounterAmount(@RequestBody @Valid CounterAmount counterAmount) {
		return counterService.insertCounterAmount(counterAmount)
			.map(updatedCounterId -> ResponseEntity.noContent().build())
			.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
