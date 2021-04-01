package com.example.spring_security.demo.security.oauth2.client_providers.impl;

import com.example.spring_security.demo.security.oauth2.client_providers.OAuth2ClientProvider;
import com.example.spring_security.demo.security.util.OAuth2Util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.POST;

@Component
@RequiredArgsConstructor
public class Vk implements OAuth2ClientProvider {

    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

    private final OAuth2ClientProperties oAuth2ClientProperties;

    @Getter
    private final String registrationId = "vk";

    @Override
    public ClientRegistration getRegistration() {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientAuthenticationMethod(POST)
                .authorizationGrantType(AUTHORIZATION_CODE)
                .redirectUriTemplate(DEFAULT_REDIRECT_URL)
                .clientId(OAuth2Util.getClientId(oAuth2ClientProperties, registrationId))
                .clientSecret(OAuth2Util.getClientSecret(oAuth2ClientProperties, registrationId))
                .scope("email")
                .clientName("VK")
                .authorizationUri("https://oauth.vk.com/authorize?v=5.95")
                .tokenUri("https://oauth.vk.com/access_token")
                .userInfoUri("https://api.vk.com/method/users.get?v=5.95")
                .userNameAttributeName("user_id")
                .build();
    }

}
