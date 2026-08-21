package com.wow_libre.register_discord.infrastructure.soap.trinity.resp;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public Envelope createEnvelope() {
        return new Envelope();
    }

    public Envelope.Body createEnvelopeBody() {
        return new Envelope.Body();
    }

    public ExecuteCommandResponse createExecuteCommandResponse() {
        return new ExecuteCommandResponse();
    }
}
