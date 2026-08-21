package com.wow_libre.register_discord.infrastructure.discord;

import com.wow_libre.register_discord.application.register.RegisterAccountService;
import com.wow_libre.register_discord.application.register.RegistrationRateLimiter;
import com.wow_libre.register_discord.domain.exception.RateLimitException;
import com.wow_libre.register_discord.domain.exception.RegisterException;
import com.wow_libre.register_discord.domain.model.RegisterAccountCommand;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RegisterDiscordListener extends ListenerAdapter {

    public static final String COMMAND_NAME = "registrar";
    public static final String MODAL_ID = "wow-register-modal";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_EMAIL = "email";

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterDiscordListener.class);

    private final RegisterAccountService registerAccountService;
    private final RegistrationRateLimiter rateLimiter;
    private final DiscordCommandRegistrar commandRegistrar;

    public RegisterDiscordListener(RegisterAccountService registerAccountService,
                                   RegistrationRateLimiter rateLimiter,
                                   DiscordCommandRegistrar commandRegistrar) {
        this.registerAccountService = registerAccountService;
        this.rateLimiter = rateLimiter;
        this.commandRegistrar = commandRegistrar;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        commandRegistrar.register(event.getJDA());
        LOGGER.info("Bot conectado como {}", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!COMMAND_NAME.equals(event.getName())) {
            return;
        }
        try {
            rateLimiter.assertCanStart(event.getUser().getId());
        } catch (RateLimitException e) {
            event.reply(formatRateLimit(e)).setEphemeral(true).queue();
            return;
        }

        TextInput username = TextInput.create(FIELD_USERNAME, "Usuario", TextInputStyle.SHORT)
                .setPlaceholder("Entre 3 y 16 letras o números")
                .setRequiredRange(3, 16)
                .setRequired(true)
                .build();
        TextInput password = TextInput.create(FIELD_PASSWORD, "Contraseña", TextInputStyle.SHORT)
                .setPlaceholder("Entre 6 y 16 caracteres")
                .setRequiredRange(6, 16)
                .setRequired(true)
                .build();
        TextInput email = TextInput.create(FIELD_EMAIL, "Correo", TextInputStyle.SHORT)
                .setPlaceholder("tu@email.com")
                .setRequiredRange(5, 64)
                .setRequired(true)
                .build();

        Modal modal = Modal.create(MODAL_ID, "Registro de cuenta WoW")
                .addComponents(ActionRow.of(username), ActionRow.of(password), ActionRow.of(email))
                .build();
        event.replyModal(modal).queue();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!MODAL_ID.equals(event.getModalId())) {
            return;
        }
        event.deferReply(true).queue();

        String username = value(event, FIELD_USERNAME);
        String password = value(event, FIELD_PASSWORD);
        String email = value(event, FIELD_EMAIL);

        try {
            String realmlist = registerAccountService.register(new RegisterAccountCommand(
                    event.getUser().getId(), username, password, email));
            event.getHook().sendMessage(successMessage(username, realmlist)).queue();
        } catch (RateLimitException e) {
            event.getHook().sendMessage(formatRateLimit(e)).queue();
        } catch (IllegalArgumentException e) {
            event.getHook().sendMessage(e.getMessage()).queue();
        } catch (RegisterException e) {
            event.getHook().sendMessage(e.getMessage()).queue();
        } catch (Exception e) {
            LOGGER.error("Error inesperado al registrar", e);
            event.getHook().sendMessage("Ocurrió un error inesperado. Inténtalo más tarde.").queue();
        }
    }

    private static String value(ModalInteractionEvent event, String id) {
        var mapping = event.getValue(id);
        return mapping == null ? "" : mapping.getAsString().trim();
    }

    private static String formatRateLimit(RateLimitException e) {
        long minutes = Math.max(1, TimeUnit.SECONDS.toMinutes(Math.max(1, e.getRetryAfter().toSeconds())));
        return e.getMessage() + " Tiempo de espera aproximado: " + minutes + " min.";
    }

    private static String successMessage(String username, String realmlist) {
        return """
                Cuenta **%s** creada correctamente.

                Para entrar al reino, en el launcher o en `WTF/Config.wtf` usa:

                ```
                %s
                ```
                Guarda tu usuario y contraseña. No los compartas en el chat.
                """.formatted(username, realmlist);
    }

    public static net.dv8tion.jda.api.interactions.commands.build.SlashCommandData commandData() {
        return Commands.slash(COMMAND_NAME, "Registra una cuenta de World of Warcraft");
    }
}
