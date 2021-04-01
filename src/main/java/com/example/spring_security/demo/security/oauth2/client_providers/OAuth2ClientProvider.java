package com.example.spring_security.demo.security.oauth2.client_providers;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

public interface OAuth2ClientProvider {
    String getRegistrationId();
    ClientRegistration getRegistration();
}
