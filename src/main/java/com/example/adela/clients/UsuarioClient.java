package com.example.adela.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.adela.dto.EstudianteDTO;
import com.example.adela.dto.ProfesorDTO;

@FeignClient(name = "ms-auth", url = "http://localhost:8081")
public interface UsuarioClient {
    @GetMapping("/api/estudiantes/{email}")
    EstudianteDTO obtenerEstudiante(@PathVariable("email") String email);

    @GetMapping("/api/profesores/{email}")
    ProfesorDTO obtenerProfesor(@PathVariable("email") String email);
}
