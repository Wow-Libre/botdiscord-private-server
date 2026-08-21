package com.wow_libre.register_discord.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        DiscordProperties.class,
        WowProperties.class,
        SoapProperties.class,
        RegisterProperties.class
})
public class AppPropertiesConfig {
}
