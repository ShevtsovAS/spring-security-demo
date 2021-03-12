package com.example.spring_security.demo.security;

import com.example.spring_security.demo.exeptions.JwtAuthenticationException;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class JwtTokenFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        Optional.ofNullable(jwtTokenProvider.resolveToken(request))
                .filter(token -> validateToken(token, response))
                .map(token -> jwtTokenProvider.getAuthentication(token, request))
                .ifPresent(this::setAuthentication);
        chain.doFilter(request, response);
    }

    @SneakyThrows
    private boolean validateToken(String token, ServletResponse servletResponse) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) servletResponse).sendError(e.getHttpStatus().value());
            throw e;
        }
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
