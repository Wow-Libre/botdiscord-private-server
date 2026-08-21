package com.wow_libre.register_discord.domain.model;

public record RegisterAccountCommand(
        String discordUserId,
        String username,
        String password,
        String email
) {
}
