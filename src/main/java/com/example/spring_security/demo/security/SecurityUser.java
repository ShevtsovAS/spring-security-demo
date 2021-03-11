package com.example.spring_security.demo.security;

import com.example.spring_security.demo.model.Status;
import com.example.spring_security.demo.model.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final boolean active;

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public static SecurityUser of(User user) {
        return SecurityUser.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .active(user.getStatus() == Status.ACTIVE)
                .authorities(user.getRole().getAuthorities())
                .build();
    }
}
