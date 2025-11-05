package com.example.chaea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoDTO {
    private String nombre;
    private List<EstudianteCrearDTO> estudiantes;
}