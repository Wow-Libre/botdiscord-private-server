package com.wow_libre.register_discord.infrastructure.discord;

import com.wow_libre.register_discord.infrastructure.config.DiscordProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "discord.bot", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DiscordBotConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBotConfig.class);

    @Bean(destroyMethod = "shutdown")
    public JDA jda(DiscordProperties discordProperties, RegisterDiscordListener listener) throws InterruptedException {
        if (discordProperties.getToken() == null || discordProperties.getToken().isBlank()) {
            throw new IllegalStateException(
                    "Falta discord.bot.token o la variable DISCORD_BOT_TOKEN.");
        }
        JDA jda = JDABuilder.createLight(discordProperties.getToken())
                .addEventListeners(listener)
                .build();
        jda.awaitReady();
        LOGGER.info("JDA listo");
        return jda;
    }
}
