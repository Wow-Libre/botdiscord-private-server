package com.wow_libre.register_discord.infrastructure.soap.xml;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public Envelope createEnvelope() {
        return new Envelope();
    }

    public Body createBody() {
        return new Body();
    }

    public ExecuteCommand createExecuteCommand() {
        return new ExecuteCommand();
    }
}
