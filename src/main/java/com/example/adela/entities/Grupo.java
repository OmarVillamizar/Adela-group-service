package com.example.adela.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String profesorId; // email or id of the professor

    @ElementCollection
    private Set<String> estudianteIds = new HashSet<>(); // emails or ids of the students

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Grupo))
            return false;
        Grupo grupo = (Grupo) o;
        return getId() == grupo.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}