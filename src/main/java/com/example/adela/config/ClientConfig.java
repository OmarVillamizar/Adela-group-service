package com.example.adela.config;

import com.example.adela.clients.UsuarioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                // Log para debug
                System.out.println("Llamando a: " + clientRequest.url());
                return Mono.just(clientRequest);
            }))
            .build();
    }

    @Bean
    public UsuarioClient usuarioClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build();

        return factory.createClient(UsuarioClient.class);
    }
}