package com.example.chaea.entities;

import com.example.chaea.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_referencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioReferencia {

    @Id
    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;
}
