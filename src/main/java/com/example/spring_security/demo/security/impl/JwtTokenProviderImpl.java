package com.example.spring_security.demo.security.impl;

import com.example.spring_security.demo.config.JwtConfig;
import com.example.spring_security.demo.exeptions.JwtAuthenticationException;
import com.example.spring_security.demo.security.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final JwtConfig jwtConfig;

    public String createToken(Authentication authentication) {
        val now = LocalDateTime.now();
        val validity = now.plus(jwtConfig.getExpiration());
        val authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(validity))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    private boolean validateToken(String token) {
        try {
            val jwtClaims = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
            return new Date().before(jwtClaims.getBody().getExpiration());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token {} is not valid", token, e);
            throw new JwtAuthenticationException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        return Optional.ofNullable(resolveToken(request))
                .filter(this::validateToken)
                .map(this::getAuthentication)
                .map(authentication -> {
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    return authentication;
                }).orElse(null);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        val userDetails = User
                .withUsername(getUserName(token))
                .password("")
                .authorities(getAuthorities(token))
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String resolveToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtConfig.getHeader()))
                .filter(jwtHeader -> jwtHeader.startsWith(jwtConfig.getPrefix()))
                .map(jwtHeader -> jwtHeader.replace(jwtConfig.getPrefix(), ""))
                .orElse(null);
    }

    private String getUserName(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked") // jjwt ???????????????????? ?????????????????? ?????????????????? ?? ?????????? ???????????????????? ???????????????? ???????????? ???? ?????????????? claims ????????????
    private String[] getAuthorities(String token) {
        return ((ArrayList<String>) Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .get("authorities"))
                .toArray(String[]::new);
    }

}
