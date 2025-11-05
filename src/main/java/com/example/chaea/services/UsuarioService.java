package com.example.chaea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.chaea.clients.UsuarioClient;
import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.dto.ProfesorDTO;

import java.util.List;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioClient usuarioClient;
    
    public ProfesorDTO obtenerProfesor(String email) {
        try {
            return usuarioClient.obtenerProfesor(email);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener profesor desde ms-auth: " + e.getMessage(), e);
        }
    }
    
    public EstudianteDTO obtenerEstudiante(String email) {
        try {
            return usuarioClient.obtenerEstudiante(email);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estudiante desde ms-auth: " + e.getMessage(), e);
        }
    }
    
    public List<EstudianteDTO> obtenerTodosEstudiantes() {
        try {
            return usuarioClient.obtenerTodosEstudiantes();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estudiantes desde ms-auth: " + e.getMessage(), e);
        }
    }
}