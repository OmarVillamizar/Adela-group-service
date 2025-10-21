package com.example.chaea.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfigGrupos {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilita CSRF para permitir llamadas POST desde otros servicios
            .csrf(csrf -> csrf.disable())
            
            // Configura las reglas de autorización
            .authorizeHttpRequests(authorize -> authorize
                // 🛡️ Permitir acceso público al endpoint de referencia
                .requestMatchers(HttpMethod.POST, "/api/usuario-referencia").permitAll()
                
                // 🔒 Requiere autenticación para cualquier otra ruta
                .anyRequest().authenticated()
            );

        // Puedes añadir otras configuraciones aquí si usas JWT, CORS, etc.

        return http.build();
    }
}