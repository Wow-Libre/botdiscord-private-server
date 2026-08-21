package com.wow_libre.register_discord.application.register;

import com.wow_libre.register_discord.domain.exception.RegisterException;
import com.wow_libre.register_discord.domain.model.RegisterAccountCommand;
import com.wow_libre.register_discord.domain.ports.ExecuteSoapCommandPort;
import com.wow_libre.register_discord.infrastructure.config.WowProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RegisterAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterAccountService.class);

    private final ExecuteSoapCommandPort executeSoapCommandPort;
    private final RegistrationRateLimiter rateLimiter;
    private final WowProperties wowProperties;
    private final Set<String> inFlight = ConcurrentHashMap.newKeySet();

    public RegisterAccountService(ExecuteSoapCommandPort executeSoapCommandPort,
                                  RegistrationRateLimiter rateLimiter,
                                  WowProperties wowProperties) {
        this.executeSoapCommandPort = executeSoapCommandPort;
        this.rateLimiter = rateLimiter;
        this.wowProperties = wowProperties;
    }

    public String register(RegisterAccountCommand command) {
        rateLimiter.assertCanStart(command.discordUserId());
        AccountCommandFactory.validate(command.username(), command.password(), command.email());
        if (!inFlight.add(command.discordUserId())) {
            throw new RegisterException("Ya hay un registro en curso. Espera a que termine.");
        }
        rateLimiter.recordAttempt(command.discordUserId());
        try {
            String soapCommand = AccountCommandFactory.createAccountCommand(
                    command.username(), command.password(), command.email());
            try {
                executeSoapCommandPort.execute(soapCommand);
            } catch (Exception e) {
                LOGGER.error("Fallo SOAP al registrar usuario Discord {}", command.discordUserId(), e);
                throw new RegisterException(mapSoapError(e), e);
            }
            rateLimiter.recordSuccess(command.discordUserId());
            return wowProperties.getRealmlist();
        } finally {
            inFlight.remove(command.discordUserId());
        }
    }

    private String mapSoapError(Exception e) {
        String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
        if (message.contains("already") || message.contains("exist") || message.contains("in use")) {
            return "Ese usuario ya está registrado en el servidor.";
        }
        if (message.contains("auth") || message.contains("401") || message.contains("unauthorized")) {
            return "No se pudo autenticar con el SOAP del reino. Revisa usuario y contraseña GM.";
        }
        return "No se pudo crear la cuenta. Inténtalo más tarde o contacta a un administrador.";
    }
}
