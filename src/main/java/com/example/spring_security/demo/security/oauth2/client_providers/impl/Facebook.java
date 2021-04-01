package com.example.spring_security.demo.security.oauth2.client_providers.impl;

import com.example.spring_security.demo.security.oauth2.client_providers.OAuth2ClientProvider;
import com.example.spring_security.demo.security.util.OAuth2Util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.FACEBOOK;

@Component
@RequiredArgsConstructor
public class Facebook implements OAuth2ClientProvider {

    private final OAuth2ClientProperties oAuth2ClientProperties;

    @Getter
    private final String registrationId = "facebook";

    @Override
    public ClientRegistration getRegistration() {
        return FACEBOOK.getBuilder(registrationId)
                .clientId(OAuth2Util.getClientId(oAuth2ClientProperties, registrationId))
                .clientSecret(OAuth2Util.getClientSecret(oAuth2ClientProperties, registrationId))
                .build();
    }

}
