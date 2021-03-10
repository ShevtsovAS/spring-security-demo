package com.example.spring_security.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.spring_security.demo.model.Role.ADMIN;
import static com.example.spring_security.demo.model.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REST_API_END_POINT = "/api/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(GET, REST_API_END_POINT).hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers(POST, REST_API_END_POINT).hasRole(ADMIN.name())
                .antMatchers(DELETE, REST_API_END_POINT).hasRole(ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .logout();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password("$2y$12$jC9Jr4Jsa4m1WhnKqJOuxevrhLWJTts.yS8EO0zfDZykwB9uk/9MW")
                        .roles(ADMIN.name())
                        .build(),
                User.builder()
                        .username("user")
                        .password("$2y$12$xo6H1DazijkctHqFPYqNQODirwKQ/B1aRVO2j3olxK/LKQnqCkpuC")
                        .roles(USER.name())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
