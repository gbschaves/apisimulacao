package com.testest.apisimulacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Lista de todos os caminhos que devem ser públicos.
                        .requestMatchers(
                                "/swagger-ui.html",      // A página principal do Swagger
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/openapi-examples/**"   // A pasta com exemplos de payload
                        ).permitAll()

                        // Para qualquer outra requisição
                        .anyRequest().permitAll() // ou .anyRequest().authenticated()
                );

        return http.build();
    }
}