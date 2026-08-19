package com.atlas.marketplace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!local & !test")
public class ProductionSecurityConfig {
    @Bean SecurityFilterChain denyUntilSsoDecision(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(a -> a.requestMatchers("/actuator/health/**").permitAll().anyRequest().denyAll()).build();
    }
}
