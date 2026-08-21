package com.wow_libre.register_discord.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmulatorCoreTest {

    @Test
    void parsesKnownNames() {
        assertEquals(EmulatorCore.AZEROTH_CORE, EmulatorCore.fromName("AzerothCore"));
        assertEquals(EmulatorCore.TRINITY_CORE, EmulatorCore.fromName("trinitycore"));
    }

    @Test
    void defaultsWhenBlank() {
        assertEquals(EmulatorCore.AZEROTH_CORE, EmulatorCore.fromName(" "));
    }

    @Test
    void rejectsUnknown() {
        assertThrows(IllegalArgumentException.class, () -> EmulatorCore.fromName("Mangos"));
    }
}
