package com.wow_libre.register_discord.infrastructure.soap;

import com.wow_libre.register_discord.infrastructure.config.SoapProperties;
import com.wow_libre.register_discord.infrastructure.soap.trinity.ExecuteCommand;
import com.wow_libre.register_discord.infrastructure.soap.xml.Body;
import com.wow_libre.register_discord.infrastructure.soap.xml.Envelope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller azerothRequestMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                com.wow_libre.register_discord.infrastructure.soap.xml.ExecuteCommand.class,
                Envelope.class,
                Body.class,
                com.wow_libre.register_discord.infrastructure.soap.xml.ObjectFactory.class);
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller azerothResponseUnmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(
                com.wow_libre.register_discord.infrastructure.soap.xml.resp.ExecuteCommandResponse.class,
                com.wow_libre.register_discord.infrastructure.soap.xml.resp.Envelope.class,
                com.wow_libre.register_discord.infrastructure.soap.xml.resp.ObjectFactory.class);
        return unmarshaller;
    }

    @Bean
    public Jaxb2Marshaller trinityRequestMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                ExecuteCommand.class,
                com.wow_libre.register_discord.infrastructure.soap.trinity.Envelope.class,
                com.wow_libre.register_discord.infrastructure.soap.trinity.Body.class,
                com.wow_libre.register_discord.infrastructure.soap.trinity.ObjectFactory.class);
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller trinityResponseUnmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(
                com.wow_libre.register_discord.infrastructure.soap.trinity.resp.ExecuteCommandResponse.class,
                com.wow_libre.register_discord.infrastructure.soap.trinity.resp.Envelope.class,
                com.wow_libre.register_discord.infrastructure.soap.trinity.resp.ObjectFactory.class);
        return unmarshaller;
    }

    @Bean(name = "auth_azeroth_core")
    public WebServiceTemplate azerothCoreTemplate(
            @Qualifier("azerothRequestMarshaller") Jaxb2Marshaller requestMarshaller,
            @Qualifier("azerothResponseUnmarshaller") Jaxb2Marshaller responseUnmarshaller,
            WebServiceMessageSenderWithAuth auth,
            SoapProperties soapProperties) {
        return buildTemplate(requestMarshaller, responseUnmarshaller, auth, soapProperties.getUri());
    }

    @Bean(name = "auth_trinity_core")
    public WebServiceTemplate trinityCoreTemplate(
            @Qualifier("trinityRequestMarshaller") Jaxb2Marshaller requestMarshaller,
            @Qualifier("trinityResponseUnmarshaller") Jaxb2Marshaller responseUnmarshaller,
            WebServiceMessageSenderWithAuth auth,
            SoapProperties soapProperties) {
        return buildTemplate(requestMarshaller, responseUnmarshaller, auth, soapProperties.getUri());
    }

    private WebServiceTemplate buildTemplate(Jaxb2Marshaller marshaller,
                                             Jaxb2Marshaller unmarshaller,
                                             WebServiceMessageSenderWithAuth auth,
                                             String uri) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(unmarshaller);
        template.setMessageSender(auth);
        template.setInterceptors(new ClientInterceptor[]{new SoapLoggingInterceptor()});
        template.setDefaultUri(uri);
        return template;
    }
}
