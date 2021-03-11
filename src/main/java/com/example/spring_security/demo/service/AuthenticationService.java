package com.example.spring_security.demo.service;

import com.example.spring_security.demo.rest.AuthenticationRequestDto;
import com.example.spring_security.demo.rest.AuthenticationResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponse getToken(AuthenticationRequestDto authRequest);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
