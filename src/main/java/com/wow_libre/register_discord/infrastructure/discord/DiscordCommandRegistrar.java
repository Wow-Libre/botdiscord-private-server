package com.wow_libre.register_discord.infrastructure.discord;

import com.wow_libre.register_discord.infrastructure.config.DiscordProperties;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiscordCommandRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordCommandRegistrar.class);

    private final DiscordProperties discordProperties;

    public DiscordCommandRegistrar(DiscordProperties discordProperties) {
        this.discordProperties = discordProperties;
    }

    public void register(JDA jda) {
        var command = RegisterDiscordListener.commandData();
        String guildId = discordProperties.getGuildId();
        if (guildId != null && !guildId.isBlank()) {
            var guild = jda.getGuildById(guildId.trim());
            if (guild == null) {
                LOGGER.warn("No se encontró el guild {}. El comando se registrará de forma global.", guildId);
                jda.updateCommands().addCommands(command).queue();
                return;
            }
            guild.updateCommands().addCommands(command).queue();
            LOGGER.info("Comando /{} registrado en el guild {}", RegisterDiscordListener.COMMAND_NAME, guildId);
            return;
        }
        jda.updateCommands().addCommands(command).queue();
        LOGGER.info("Comando /{} registrado de forma global (puede tardar hasta una hora)",
                RegisterDiscordListener.COMMAND_NAME);
    }
}
