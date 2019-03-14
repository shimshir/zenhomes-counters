package de.admir.zenhomes.controllers;

import de.admir.zenhomes.models.data.CounterReport;
import de.admir.zenhomes.services.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consumption-report")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumptionReportController {
	private final CounterService counterService;

	@GetMapping
	public List<CounterReport> getReport() {
		return counterService.consumptionReport();
	}
}
