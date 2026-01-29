package com.giuzep89.helpinghandbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final MyUserDetailsService userDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, MyUserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/*/profile-picture").permitAll()

                        // Admin (method-level @PreAuthorize handles role check)
                        .requestMatchers("/posts/admin/**").authenticated()
                        .requestMatchers("/users/admin/**").authenticated()

                        // Posts
                        .requestMatchers(HttpMethod.GET, "/posts").authenticated()
                        .requestMatchers(HttpMethod.POST, "/posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/posts/*").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/posts/**").authenticated()

                        // Chats
                        .requestMatchers("/chats/**").authenticated()

                        // Users
                        .requestMatchers(HttpMethod.GET, "/users").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/*").authenticated()
                        .requestMatchers("/users/*/friends/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/*/profile-picture").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/users/*/profile-picture").authenticated()

                        .anyRequest().denyAll()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
