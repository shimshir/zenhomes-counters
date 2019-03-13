package de.admir.zenhomes.controllers;

import de.admir.zenhomes.services.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counter-amounts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CounterAmountController {
	private final CounterService counterService;

	@PostMapping
	public ResponseEntity postCounterAmount(@RequestParam("counter-id") String counterId, @RequestBody Double amount) {
		if (amount < 0) {
			return ResponseEntity.badRequest().body("The amount has to be positive or zero");
		} else {
			return counterService.insertCounterAmount(counterId, amount)
				.map(updatedCounterId -> ResponseEntity.noContent().build())
				.orElseGet(() -> ResponseEntity.notFound().build());
		}
	}
}
