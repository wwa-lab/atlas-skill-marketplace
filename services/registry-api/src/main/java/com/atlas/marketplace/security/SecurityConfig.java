package com.atlas.marketplace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile({"local", "test"})
public class SecurityConfig {
    @Bean InMemoryUserDetailsManager users() {
        return new InMemoryUserDetailsManager(User.withUsername("atlas-local").password("{noop}local-only").roles("CONSUMER", "OWNER", "SME").build());
    }
    @Bean SecurityFilterChain localSecurity(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(a -> a.requestMatchers("/actuator/health/**").permitAll().anyRequest().authenticated())
            .httpBasic(b -> {}).csrf(c -> c.ignoringRequestMatchers("/api/v1/**")).build();
    }
}
