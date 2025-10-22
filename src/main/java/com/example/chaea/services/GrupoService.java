package com.example.chaea.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chaea.clients.UsuarioClient;
import com.example.chaea.dto.EstudianteDTO;
import com.example.chaea.dto.EstudianteEmailDTO;
import com.example.chaea.dto.GrupoDTO;
import com.example.chaea.dto.GrupoResponseDTO;
import com.example.chaea.dto.ProfesorDTO;
import com.example.chaea.entities.Grupo;
import com.example.chaea.repositories.GrupoRepository;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public Grupo crearGrupo(GrupoDTO dto) {
        Grupo grupo = new Grupo();
        grupo.setNombre(dto.getNombre());
        grupo.setProfesorId(dto.getProfesorEmail());

        // Guardamos solo los emails
        if (dto.getEstudiantes() != null) {
            grupo.setEstudianteIds(
                dto.getEstudiantes().stream()
                    .map(EstudianteEmailDTO::getEmail)
                    .collect(Collectors.toSet())
            );
        }

        return grupoRepository.save(grupo);
    }
    
    @Autowired
    private UsuarioClient usuarioClient;

    public GrupoResponseDTO mapToResponse(Grupo grupo) {
        List<EstudianteDTO> estudiantes = grupo.getEstudianteIds().stream()
                .map(usuarioClient::obtenerEstudiante) // Ej: usuarioClient.obtenerEstudiante(email)
                .toList();

        ProfesorDTO profesor = usuarioClient.obtenerProfesor(grupo.getProfesorId());

        return new GrupoResponseDTO(
            grupo.getId(),
            grupo.getNombre(),
            profesor,
            estudiantes,
            estudiantes.size()
        );
    }

    public List<GrupoResponseDTO> obtenerGrupos() {
        return grupoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
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
            grupo.setProfesorId(grupoDTO.getProfesorEmail());
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
