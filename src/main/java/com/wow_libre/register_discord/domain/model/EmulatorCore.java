package com.wow_libre.register_discord.domain.model;

import java.util.Arrays;

public enum EmulatorCore {
    TRINITY_CORE("TrinityCore"),
    AZEROTH_CORE("AzerothCore");

    private final String name;

    EmulatorCore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EmulatorCore fromName(String name) {
        if (name == null || name.isBlank()) {
            return AZEROTH_CORE;
        }
        return Arrays.stream(values())
                .filter(core -> core.name.equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Emulador no soportado: " + name + ". Use AzerothCore o TrinityCore"));
    }
}
