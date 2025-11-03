
package com.example.adela.repositories;

import com.example.adela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
    
    List<Grupo> findByNombre(String nombre);
    
    List<Grupo> findByProfesorEmail(String profesorEmail);
    
    Optional<Grupo> findByProfesorEmailAndId(String profesorEmail, int id);
}