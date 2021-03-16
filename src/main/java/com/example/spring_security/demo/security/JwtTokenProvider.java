package com.example.spring_security.demo.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {
    String createToken(Authentication authentication);

    Authentication getAuthentication(HttpServletRequest request);
}
