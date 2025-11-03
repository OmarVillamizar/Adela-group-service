package com.example.adela.services;

import com.example.adela.clients.UsuarioClient;
import com.example.adela.dto.EstudianteDTO;
import com.example.adela.dto.ProfesorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UsuarioService {
    
    @Autowired
    private WebClient webClient;
    
    private String getAuthorizationHeader() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            return authHeader != null ? authHeader : "";
        }
        return "";
    }
    
    public ProfesorDTO obtenerProfesor(String email) {
        try {
            return webClient.get()
                .uri("/ms-auth/profesores/" + email)
                .header("Authorization", getAuthorizationHeader())
                .retrieve()
                .bodyToMono(ProfesorDTO.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al obtener profesor: " + e.getMessage());
        }
    }
    
    public EstudianteDTO obtenerEstudiante(String email) {
        try {
            return webClient.get()
                .uri("/ms-auth/estudiantes/" + email)
                .header("Authorization", getAuthorizationHeader())
                .retrieve()
                .bodyToMono(EstudianteDTO.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al obtener estudiante: " + e.getMessage());
        }
    }
    
    public List<EstudianteDTO> obtenerTodosEstudiantes() {
        try {
            return webClient.get()
                .uri("/ms-auth/estudiantes")
                .header("Authorization", getAuthorizationHeader())
                .retrieve()
                .bodyToFlux(EstudianteDTO.class)
                .collectList()
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al obtener estudiantes: " + e.getMessage());
        }
    }
}