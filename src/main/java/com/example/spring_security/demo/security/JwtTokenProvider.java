package com.example.spring_security.demo.security;

import com.example.spring_security.demo.config.JwtConfig;
import com.example.spring_security.demo.exeptions.JwtAuthenticationException;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;

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

    public boolean validateToken(String token) {
        try {
            val jwtClaims = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
            return new Date().before(jwtClaims.getBody().getExpiration());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token {} is not valid", token, e);
            throw new JwtAuthenticationException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(String token, HttpServletRequest request) {
        val userDetails = userDetailsService.loadUserByUsername(getUserName(token));
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    public String resolveToken(HttpServletRequest request) {
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

}
