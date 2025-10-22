package com.example.chaea.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chaea.dto.GrupoDTO;
import com.example.chaea.dto.GrupoResponseDTO;
import com.example.chaea.entities.Grupo;
import com.example.chaea.services.GrupoService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    // Crear un nuevo grupo
    @PostMapping
    public GrupoResponseDTO crearGrupo(@RequestBody GrupoDTO dto) {
        Grupo grupoGuardado = grupoService.crearGrupo(dto);
        return grupoService.mapToResponse(grupoGuardado);
    }


    
    @GetMapping
    public List<GrupoResponseDTO> getGrupos() {
        return grupoService.obtenerGrupos();
    }


    // Consultar un grupo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> consultarGrupoPorId(@PathVariable int id) {
        Grupo grupo = grupoService.consultarGrupoPorId(id);
        if (grupo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo no encontrado con el ID: " + id);
        }
        return ResponseEntity.ok(grupo);
    }

    // Eliminar un grupo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarGrupo(@PathVariable int id) {
        boolean eliminado = grupoService.eliminarGrupo(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo no encontrado con el ID: " + id);
        }
        return ResponseEntity.ok("Grupo eliminado exitosamente.");
    }

    // Actualizar un grupo
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarGrupo(@PathVariable int id, @RequestBody GrupoDTO grupoDTO) {
        Grupo grupoActualizado = grupoService.actualizarGrupo(id, grupoDTO);
        if (grupoActualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo no encontrado con el ID: " + id);
        }
        return ResponseEntity.ok(grupoActualizado);
    }

    // Agregar estudiantes a un grupo
    @PostMapping("/{id}/estudiantes")
    public ResponseEntity<?> agregarEstudiantesAlGrupo(@PathVariable int id, @RequestBody Set<String> estudianteIds) {
        Grupo grupo = grupoService.agregarEstudiantesAlGrupo(id, estudianteIds);
        if (grupo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo no encontrado con el ID: " + id);
        }
        return ResponseEntity.ok(grupo);
    }

    // Eliminar estudiantes de un grupo
    @DeleteMapping("/{id}/estudiantes")
    public ResponseEntity<?> eliminarEstudiantesDelGrupo(@PathVariable int id, @RequestBody Set<String> estudianteIds) {
        Grupo grupo = grupoService.eliminarEstudiantesDelGrupo(id, estudianteIds);
        if (grupo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo no encontrado con el ID: " + id);
        }
        return ResponseEntity.ok(grupo);
    }
}
