package com.wow_libre.register_discord.infrastructure.soap;

import com.wow_libre.register_discord.infrastructure.config.SoapProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

    private final SoapProperties soapProperties;

    public WebServiceMessageSenderWithAuth(SoapProperties soapProperties) {
        this.soapProperties = soapProperties;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws IOException {
        String credentials = soapProperties.getUsername() + ":" + soapProperties.getPassword();
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
        super.prepareConnection(connection);
    }
}
