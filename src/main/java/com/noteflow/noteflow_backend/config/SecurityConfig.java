package com.noteflow.noteflow_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF voor REST API
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").permitAll() // Allow alle API calls
                        .requestMatchers("/h2-console/**").permitAll() // Allow H2 console
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Voor H2 console
                );

        return http.build();
    }
}