package com.example.chaea.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.example.chaea.dto.EstudianteCrearDTO;
import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.dto.ProfesorDTO;

import java.util.List;

@HttpExchange("/ms-auth")
public interface UsuarioClient {

    @GetExchange("/profesores/{email}")
    ProfesorDTO obtenerProfesor(@PathVariable("email") String email);

    @GetExchange("/estudiantes/{email}")
    EstudianteDTO obtenerEstudiante(@PathVariable("email") String email);

    @GetExchange("/estudiantes")
    List<EstudianteDTO> obtenerTodosEstudiantes();
    
    @PostExchange("/estudiantes/create")
    List<EstudianteDTO> crearCascaras(@RequestBody List<EstudianteCrearDTO> estudiantes);
}