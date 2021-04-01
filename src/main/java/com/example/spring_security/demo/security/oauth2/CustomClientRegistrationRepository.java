package com.example.spring_security.demo.security.oauth2;

import com.example.spring_security.demo.security.oauth2.client_providers.OAuth2ClientProvider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Repository
public class CustomClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {

    private final Map<String, ClientRegistration> registrations;

    public CustomClientRegistrationRepository(List<OAuth2ClientProvider> providers, OAuth2ClientProperties oAuth2ClientProperties) {
        this.registrations = providers.stream()
                .filter(it -> oAuth2ClientProperties.getRegistration().containsKey(it.getRegistrationId()))
                .map(OAuth2ClientProvider::getRegistration)
                .collect(toUnmodifiableMap(ClientRegistration::getRegistrationId, Function.identity()));
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return Optional.ofNullable(registrations.get(registrationId)).orElseThrow();
    }

    @Override
    @NonNull
    public Iterator<ClientRegistration> iterator() {
        return registrations.values().iterator();
    }
}
