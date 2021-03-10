package com.example.spring_security.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static com.example.spring_security.demo.model.Permission.DEVELOPERS_READ;
import static com.example.spring_security.demo.model.Permission.DEVELOPERS_WRITE;
import static java.util.stream.Collectors.toSet;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Set.of(DEVELOPERS_READ)),
    ADMIN(Set.of(DEVELOPERS_READ, DEVELOPERS_WRITE));

    private final Set<Permission> permissions;

    public Set<GrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(Permission::getValue)
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
    }
}
