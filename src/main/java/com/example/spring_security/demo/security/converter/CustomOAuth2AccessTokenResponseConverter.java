package com.example.spring_security.demo.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.endpoint.MapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.Map;

public class CustomOAuth2AccessTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {

    private final MapOAuth2AccessTokenResponseConverter mapOAuth2AccessTokenResponseConverter = new MapOAuth2AccessTokenResponseConverter();

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
        tokenResponseParameters.putIfAbsent("token_type", "Bearer");
        return mapOAuth2AccessTokenResponseConverter.convert(tokenResponseParameters);
    }
}
