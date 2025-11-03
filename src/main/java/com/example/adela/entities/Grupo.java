package com.example.adela.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String nombre;

    // Solo guardamos el email del profesor (referencia)
    @Column(name = "profesor_email", nullable = false, length = 100)
    private String profesorEmail;

    // Relación Many-to-Many con estudiantes (solo emails)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "grupo_estudiantes",
        joinColumns = @JoinColumn(name = "grupo_id")
    )
    @Column(name = "estudiante_email", length = 100)
    private Set<String> estudiantesEmails = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grupo)) return false;
        Grupo grupo = (Grupo) o;
        return getId() == grupo.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}