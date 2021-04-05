package com.example.spring_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static com.example.spring_security.demo.config.SecurityConfig.AUTHORIZATION_REQUEST_BASE_URI;
import static com.example.spring_security.demo.util.CustomCollectors.toLinkedMap;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientRegistrationRepository customClientRegistrationRepository;
    private final OAuth2ClientProperties oAuth2ClientProperties;

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

    private Map<String, String> getOAuth2AuthenticationUrls() {
        return oAuth2ClientProperties.getRegistration().keySet().stream()
                .map(customClientRegistrationRepository::findByRegistrationId)
                .collect(toLinkedMap(ClientRegistration::getClientName, this::getRegistrationUrl));
    }

    private String getRegistrationUrl(ClientRegistration registration) {
        return String.format("%s/%s", AUTHORIZATION_REQUEST_BASE_URI, registration.getRegistrationId());
    }
}
