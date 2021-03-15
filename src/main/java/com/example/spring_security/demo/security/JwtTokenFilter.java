package com.example.spring_security.demo.security;

import com.example.spring_security.demo.exeptions.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) {
        try {
            Optional.ofNullable(jwtTokenProvider.getAuthentication(request)).ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.sendError(e.getHttpStatus().value());
            throw e;
        }
        chain.doFilter(request, response);
    }

}
