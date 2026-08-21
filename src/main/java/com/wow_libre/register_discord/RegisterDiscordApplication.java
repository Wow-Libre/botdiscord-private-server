package com.wow_libre.register_discord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class RegisterDiscordApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterDiscordApplication.class, args);
	}

}
