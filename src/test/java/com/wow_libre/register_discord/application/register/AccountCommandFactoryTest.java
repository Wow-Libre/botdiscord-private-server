package com.wow_libre.register_discord.application.register;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountCommandFactoryTest {

    @Test
    void buildsCreateCommand() {
        String command = AccountCommandFactory.createAccountCommand("Hero", "secret1", "hero@mail.com");
        assertEquals("account create hero secret1 hero@mail.com", command);
    }

    @Test
    void rejectsInvalidUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> AccountCommandFactory.validate("ab", "secret1", "hero@mail.com"));
        assertThrows(IllegalArgumentException.class,
                () -> AccountCommandFactory.validate("bad user", "secret1", "hero@mail.com"));
    }

    @Test
    void rejectsShortPassword() {
        assertThrows(IllegalArgumentException.class,
                () -> AccountCommandFactory.validate("hero", "123", "hero@mail.com"));
    }

    @Test
    void rejectsInvalidEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> AccountCommandFactory.validate("hero", "secret1", "not-an-email"));
    }
}
