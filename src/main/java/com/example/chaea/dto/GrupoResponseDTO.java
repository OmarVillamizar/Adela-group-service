package com.example.chaea.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GrupoResponseDTO {
    private int id;
    private String nombre;
    private ProfesorDTO profesor;
    private List<EstudianteDTO> estudiantes;
    private int numEstudiantes;
}
