package com.example.adela.config;

import com.example.adela.clients.UsuarioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class ClientConfig {

    @Value("${ms-auth.url:http://localhost:8060}")
    private String msAuthUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(msAuthUrl)
            .defaultHeader("Content-Type", "application/json")
            .filter(jwtTokenPropagationFilter()) // ✅ Propagar token JWT
            .filter(loggingFilter()) // Log para debug
            .build();
    }

    /**
     * Filtro que propaga el token JWT de la petición actual
     */
    private ExchangeFilterFunction jwtTokenPropagationFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // Obtener el token de la petición HTTP actual
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                String authHeader = attributes.getRequest().getHeader("Authorization");
                
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    // Agregar el token a la petición hacia ms-auth
                    ClientRequest modifiedRequest = ClientRequest.from(clientRequest)
                        .header("Authorization", authHeader)
                        .build();
                    
                    System.out.println("🔐 Token JWT propagado a ms-auth");
                    return Mono.just(modifiedRequest);
                }
            }
            
            System.out.println("⚠️ No se encontró token JWT para propagar");
            return Mono.just(clientRequest);
        });
    }

    /**
     * Filtro para logging de requests
     */
    private ExchangeFilterFunction loggingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("📡 Llamando a: " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    @Bean
    public UsuarioClient usuarioClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();

        return factory.createClient(UsuarioClient.class);
    }
}