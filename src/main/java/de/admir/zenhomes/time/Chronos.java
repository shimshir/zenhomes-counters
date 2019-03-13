package de.admir.zenhomes.time;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Chronos {
	public long epochMilli() {
		return Instant.now().toEpochMilli();
	}
}
