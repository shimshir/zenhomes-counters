package de.admir.zenhomes.controllers;

import de.admir.zenhomes.models.cfg.ServerCfg;
import de.admir.zenhomes.models.data.Counter;
import de.admir.zenhomes.services.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/counters")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CounterController {
	private final ServerCfg serverCfg;
	private final CounterService counterService;

	@PostMapping
	public ResponseEntity<Void> postCounter(@RequestBody @Valid Counter counter) {
		String id = counterService.createCounter(counter);
		return ResponseEntity.created(URI.create(serverCfg.getLocation() + "/counters/" + id)).build();
	}

	@GetMapping
	@RequestMapping("/{id}")
	public ResponseEntity getCounter(@PathVariable("id") String id) {
		return counterService.findCounterById(id)
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
