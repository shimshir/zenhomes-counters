package de.admir.zenhomes.models.cfg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ServerCfg {
	@Value("${server.port}")
	private int port;
	@Value("${server.location}")
	private String location;
}
