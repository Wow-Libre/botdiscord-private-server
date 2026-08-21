package com.wow_libre.register_discord.application.commands;

import com.wow_libre.register_discord.domain.exception.RegisterException;
import com.wow_libre.register_discord.domain.model.EmulatorCore;
import com.wow_libre.register_discord.domain.ports.ExecuteSoapCommandPort;
import com.wow_libre.register_discord.infrastructure.config.WowProperties;
import com.wow_libre.register_discord.infrastructure.soap.AzerothSoapClient;
import com.wow_libre.register_discord.infrastructure.soap.TrinitySoapClient;
import org.springframework.stereotype.Service;

@Service
public class CommandsService implements ExecuteSoapCommandPort {

    private final AzerothSoapClient azerothSoapClient;
    private final TrinitySoapClient trinitySoapClient;
    private final WowProperties wowProperties;

    public CommandsService(AzerothSoapClient azerothSoapClient,
                           TrinitySoapClient trinitySoapClient,
                           WowProperties wowProperties) {
        this.azerothSoapClient = azerothSoapClient;
        this.trinitySoapClient = trinitySoapClient;
        this.wowProperties = wowProperties;
    }

    @Override
    public void execute(String command) {
        EmulatorCore core = EmulatorCore.fromName(wowProperties.getEmulator());
        switch (core) {
            case TRINITY_CORE -> trinitySoapClient.executeCommand(command);
            case AZEROTH_CORE -> azerothSoapClient.executeCommand(command);
            default -> throw new RegisterException("No hay cliente SOAP para " + core.getName());
        }
    }
}
