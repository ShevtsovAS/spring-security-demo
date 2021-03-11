package com.example.spring_security.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Base64;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {
    private String header;
    private String prefix;
    private String secret;
    private Duration expiration;

    public void setSecret(String secret) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }
}
