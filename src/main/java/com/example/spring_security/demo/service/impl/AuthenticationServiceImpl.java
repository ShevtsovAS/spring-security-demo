package com.example.spring_security.demo.service.impl;

import com.example.spring_security.demo.exeptions.JwtAuthenticationException;
import com.example.spring_security.demo.rest.AuthenticationRequestDto;
import com.example.spring_security.demo.rest.AuthenticationResponse;
import com.example.spring_security.demo.security.JwtTokenProvider;
import com.example.spring_security.demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthenticationResponse getToken(AuthenticationRequestDto authRequest) {
        try {
            val authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            val token = jwtTokenProvider.createToken(authentication);
            return AuthenticationResponse.builder()
                    .email(authentication.getName())
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            throw new JwtAuthenticationException(e.getMessage(), FORBIDDEN);
        }
    }

    @Override
    @SneakyThrows
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        val logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
        new SimpleUrlLogoutSuccessHandler().onLogoutSuccess(request, response, null);
    }
}
