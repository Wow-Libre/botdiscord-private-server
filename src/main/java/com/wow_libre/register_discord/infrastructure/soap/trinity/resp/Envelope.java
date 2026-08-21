package com.wow_libre.register_discord.infrastructure.soap.trinity.resp;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"body"})
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class Envelope {

    @XmlElement(name = "Body", required = true)
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"executeCommandResponse"})
    public static class Body {

        @XmlElement(name = "executeCommandResponse", namespace = "urn:TC", required = true)
        private ExecuteCommandResponse executeCommandResponse;

        public ExecuteCommandResponse getExecuteCommandResponse() {
            return executeCommandResponse;
        }

        public void setExecuteCommandResponse(ExecuteCommandResponse executeCommandResponse) {
            this.executeCommandResponse = executeCommandResponse;
        }
    }
}
