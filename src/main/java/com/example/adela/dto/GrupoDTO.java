package com.example.adela.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoDTO {
    private Integer id; // <-- agregar este campo
    private String nombre;
    private String profesorEmail;
    private Set<EstudianteEmailDTO> estudiantes;
}
