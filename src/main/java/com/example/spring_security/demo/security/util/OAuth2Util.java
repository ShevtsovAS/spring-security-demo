package com.example.spring_security.demo.security.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;

import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class OAuth2Util {

    public String getClientId(OAuth2ClientProperties oAuth2ClientProperties, String registrationId) {
        return getFromRegistration(oAuth2ClientProperties, registrationId, Registration::getClientId);
    }

    public String getClientSecret(OAuth2ClientProperties oAuth2ClientProperties, String registrationId) {
        return getFromRegistration(oAuth2ClientProperties, registrationId, Registration::getClientSecret);
    }

    private <T> T getFromRegistration(OAuth2ClientProperties oAuth2ClientProperties, String registrationId, Function<? super Registration, ? extends T> mapper) {
        return Optional.ofNullable(oAuth2ClientProperties.getRegistration().get(registrationId)).map(mapper).orElse(null);
    }
}
