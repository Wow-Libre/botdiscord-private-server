package com.wow_libre.register_discord.infrastructure.soap.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"executeCommand"})
public class Body {

    @XmlElement(name = "executeCommand", namespace = "urn:AC", required = true)
    private ExecuteCommand executeCommand;

    public ExecuteCommand getExecuteCommand() {
        return executeCommand;
    }

    public void setExecuteCommand(ExecuteCommand executeCommand) {
        this.executeCommand = executeCommand;
    }
}
