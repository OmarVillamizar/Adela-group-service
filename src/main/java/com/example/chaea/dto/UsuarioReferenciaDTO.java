package com.example.chaea.dto;

import lombok.Data;

@Data
public class UsuarioReferenciaDTO {
    private String email;
    private String tipoUsuario; // "PROFESOR" o "ESTUDIANTE"
}
