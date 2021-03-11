package com.example.spring_security.demo.security;

import com.example.spring_security.demo.exeptions.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        Optional.ofNullable(jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest))
                .filter(token -> validateToken(token, servletResponse))
                .map(jwtTokenProvider::getAuthentication)
                .ifPresent(this::setAuthentication);
        filterChain.doFilter(servletRequest, servletResponse);
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
