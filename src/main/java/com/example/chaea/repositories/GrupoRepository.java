
package com.example.chaea.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.chaea.entities.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    // Find groups by professor ID
    List<Grupo> findByProfesorId(String profesorId);

    // Find groups containing a specific student ID
    List<Grupo> findByEstudianteIdsContaining(String estudianteId);
}
