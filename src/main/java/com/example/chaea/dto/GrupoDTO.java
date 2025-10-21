package com.example.chaea.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoDTO {
    private String nombre;
    private String profesorId;        // email o id del profesor
    private Set<String> estudianteIds; // emails o ids de los estudiantes
}
