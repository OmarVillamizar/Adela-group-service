package com.example.adela.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ⚡ Deshabilitar CORS/CSRF estrictos
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            // ⚡ Permitir todas las peticiones
            .authorizeHttpRequests(authorize -> authorize
                // Swagger y docs públicos
                .requestMatchers("/api-docs/**", "/docs/**", "/swagger-ui/**").permitAll()

                // Endpoints de grupos abiertos
                .requestMatchers("/api/grupos/**").permitAll()

                // Todo lo demás también abierto
                .anyRequest().permitAll()
            );

        return http.build();
    }
}

