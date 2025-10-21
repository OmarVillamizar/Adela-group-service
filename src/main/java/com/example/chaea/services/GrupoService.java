package com.example.chaea.services;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chaea.dto.GrupoDTO;
import com.example.chaea.entities.Grupo;
import com.example.chaea.repositories.GrupoRepository;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public Grupo crearGrupo(GrupoDTO grupoDTO) {
        Grupo grupo = new Grupo();
        grupo.setNombre(grupoDTO.getNombre());
        grupo.setProfesorId(grupoDTO.getProfesorId());
        // estudianteIds se inicializa solo, pero podrías agregar uno aquí si existiera
        return grupoRepository.save(grupo);
    }

    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    public Grupo consultarGrupoPorId(int id) {
        return grupoRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean eliminarGrupo(int id) {
        if (grupoRepository.existsById(id)) {
            grupoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Grupo actualizarGrupo(int id, GrupoDTO grupoDTO) {
        return grupoRepository.findById(id).map(grupo -> {
            grupo.setNombre(grupoDTO.getNombre());
            grupo.setProfesorId(grupoDTO.getProfesorId());
            return grupoRepository.save(grupo);
        }).orElse(null);
    }

    @Transactional
    public Grupo agregarEstudiantesAlGrupo(int id, Set<String> estudianteIds) {
        return grupoRepository.findById(id).map(grupo -> {
            if (estudianteIds != null && !estudianteIds.isEmpty()) {
                grupo.getEstudianteIds().addAll(estudianteIds);
            }
            return grupoRepository.save(grupo);
        }).orElse(null);
    }

    @Transactional
    public Grupo eliminarEstudiantesDelGrupo(int id, Set<String> estudianteIds) {
        return grupoRepository.findById(id).map(grupo -> {
            if (estudianteIds != null && !estudianteIds.isEmpty()) {
                grupo.getEstudianteIds().removeAll(estudianteIds);
            }
            return grupoRepository.save(grupo);
        }).orElse(null);
    }
}