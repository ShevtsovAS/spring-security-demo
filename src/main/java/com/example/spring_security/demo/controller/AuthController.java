package com.example.spring_security.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.example.spring_security.demo.config.SecurityConfig.AUTHORIZATION_REQUEST_BASE_URI;
import static com.example.spring_security.demo.util.CustomCollectors.toLinkedMap;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientRegistrationRepository customClientRegistrationRepository;
    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }

    @GetMapping("/oauth2_login")
    public String getOAuth2LoginPage(Model model) {
        model.addAttribute("urls", getOAuth2AuthenticationUrls());
        return "oauth2-login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        Optional.ofNullable(client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()).ifPresent(userInfoEndpointUri -> {
            RestTemplate restTemplate = new RestTemplate();
            var request = buildUserInfoRequest(client);
            var response = restTemplate.exchange(request, Map.class);
            var userAttributes = Optional.ofNullable(response.getBody()).orElse(Collections.emptyMap());
            model.addAttribute("name", userAttributes.get("name"));
            model.addAttribute("email", userAttributes.get("email"));
        });
        return "loginSuccess";
    }

    private RequestEntity<Object> buildUserInfoRequest(OAuth2AuthorizedClient client) {
        val headers = new HttpHeaders();
        headers.setBearerAuth(client.getAccessToken().getTokenValue());
        val userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        return new RequestEntity<>(headers, HttpMethod.GET, URI.create(userInfoEndpointUri));
    }

    private Map<String, String> getOAuth2AuthenticationUrls() {
        return oAuth2ClientProperties.getRegistration().keySet().stream()
                .map(customClientRegistrationRepository::findByRegistrationId)
                .collect(toLinkedMap(ClientRegistration::getClientName, this::getRegistrationUrl));
    }

    private String getRegistrationUrl(ClientRegistration registration) {
        return String.format("%s/%s", AUTHORIZATION_REQUEST_BASE_URI, registration.getRegistrationId());
    }
}
