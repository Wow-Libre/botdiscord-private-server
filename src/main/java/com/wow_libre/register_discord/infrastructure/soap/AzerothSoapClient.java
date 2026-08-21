package com.wow_libre.register_discord.infrastructure.soap;

import com.wow_libre.register_discord.infrastructure.config.SoapProperties;
import com.wow_libre.register_discord.infrastructure.soap.xml.ExecuteCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class AzerothSoapClient {

    private final WebServiceTemplate webServiceTemplate;
    private final SoapProperties soapProperties;

    public AzerothSoapClient(@Qualifier("auth_azeroth_core") WebServiceTemplate webServiceTemplate,
                             SoapProperties soapProperties) {
        this.webServiceTemplate = webServiceTemplate;
        this.soapProperties = soapProperties;
    }

    public void executeCommand(String command) {
        ExecuteCommand executeCommand = new ExecuteCommand();
        executeCommand.setCommand(command);
        webServiceTemplate.marshalSendAndReceive(soapProperties.getUri(), executeCommand);
    }
}
