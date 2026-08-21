package com.wow_libre.register_discord;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "discord.bot.enabled=false",
        "wow.emulator=AzerothCore",
        "wow.realmlist=set realmlist logon.test.local",
        "soap.uri=http://127.0.0.1:7878",
        "soap.username=admin",
        "soap.password=test"
})
class RegisterDiscordApplicationTests {

    @Test
    void contextLoads() {
    }
}
