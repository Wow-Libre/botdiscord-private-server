package com.wow_libre.register_discord.infrastructure.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SoapLoggingInterceptor implements ClientInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapLoggingInterceptor.class);
    private static final Pattern ACCOUNT_CREATE = Pattern.compile(
            "(account create\\s+\\S+\\s+)\\S+", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean handleRequest(MessageContext messageContext) {
        log("Request XML", messageContext.getRequest());
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        log("Response XML", messageContext.getResponse());
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) {
        log("Fault XML", messageContext.getResponse());
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) {
        // no-op
    }

    private void log(String label, org.springframework.ws.WebServiceMessage message) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            String xml = ACCOUNT_CREATE.matcher(outputStream.toString(StandardCharsets.UTF_8))
                    .replaceAll("$1***");
            LOGGER.debug("{}: {}", label, xml);
        } catch (Exception e) {
            LOGGER.warn("No se pudo escribir el mensaje SOAP: {}", e.getMessage());
        }
    }
}
