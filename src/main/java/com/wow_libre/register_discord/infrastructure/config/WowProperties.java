package com.wow_libre.register_discord.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wow")
public class WowProperties {

    private String emulator = "AzerothCore";
    private String realmlist = "set realmlist logon.example.com";

    public String getEmulator() {
        return emulator;
    }

    public void setEmulator(String emulator) {
        this.emulator = emulator;
    }

    public String getRealmlist() {
        return realmlist;
    }

    public void setRealmlist(String realmlist) {
        this.realmlist = realmlist;
    }
}
