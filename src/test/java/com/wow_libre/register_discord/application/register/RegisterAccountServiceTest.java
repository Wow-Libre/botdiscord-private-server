package com.wow_libre.register_discord.application.register;

import com.wow_libre.register_discord.domain.exception.RegisterException;
import com.wow_libre.register_discord.domain.model.RegisterAccountCommand;
import com.wow_libre.register_discord.domain.ports.ExecuteSoapCommandPort;
import com.wow_libre.register_discord.infrastructure.config.RegisterProperties;
import com.wow_libre.register_discord.infrastructure.config.WowProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RegisterAccountServiceTest {

    private ExecuteSoapCommandPort soap;
    private RegisterAccountService service;

    @BeforeEach
    void setUp() {
        soap = mock(ExecuteSoapCommandPort.class);
        RegisterProperties registerProperties = new RegisterProperties();
        WowProperties wowProperties = new WowProperties();
        wowProperties.setRealmlist("set realmlist logon.wowlibre.com");
        service = new RegisterAccountService(soap, new RegistrationRateLimiter(registerProperties, Clock.systemUTC()), wowProperties);
    }

    @Test
    void returnsRealmlistOnSuccess() {
        String realmlist = service.register(new RegisterAccountCommand("1", "hero", "secret1", "hero@mail.com"));
        assertEquals("set realmlist logon.wowlibre.com", realmlist);
        verify(soap).execute("account create hero secret1 hero@mail.com");
    }

    @Test
    void mapsDuplicateAccountError() {
        doThrow(new RuntimeException("Account already exists")).when(soap).execute(anyString());
        RegisterException ex = assertThrows(RegisterException.class,
                () -> service.register(new RegisterAccountCommand("1", "hero", "secret1", "hero@mail.com")));
        assertEquals("Ese usuario ya está registrado en el servidor.", ex.getMessage());
    }

    @Test
    void preventsSecondSuccessfulAccount() {
        service.register(new RegisterAccountCommand("1", "hero", "secret1", "hero@mail.com"));
        assertThrows(Exception.class,
                () -> service.register(new RegisterAccountCommand("1", "hero2", "secret1", "hero2@mail.com")));
        verify(soap, times(1)).execute(anyString());
    }
}
