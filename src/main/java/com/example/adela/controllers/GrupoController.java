package com.example.adela.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.adela.clients.UsuarioClient;
import com.example.adela.dto.*;
import com.example.adela.entities.Grupo;
import com.example.adela.repositories.GrupoRepository;
import com.example.adela.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioClient usuarioClient;
    
    @PostMapping
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crearGrupo(@RequestBody GrupoDTO grupoDTO, HttpServletRequest request) {
        if (grupoDTO.getNombre() == null || grupoDTO.getEstudiantes() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan campos requeridos.");
        }
        
        // Obtener email del profesor autenticado desde el JWT
        String profesorEmail = (String) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        // Verificar que el profesor existe en ms-auth
        try {
            ProfesorDTO profesor = usuarioService.obtenerProfesor(profesorEmail);
            
            if (profesor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profesor no encontrado: " + profesorEmail);
            }
            
            // Crear nuevo grupo
            Grupo nuevoGrupo = new Grupo();
            nuevoGrupo.setNombre(grupoDTO.getNombre());
            nuevoGrupo.setProfesorEmail(profesorEmail);
            
            // Procesar estudiantes
            Set<String> estudiantesEmails = new HashSet<>();
            
            if (grupoDTO.getEstudiantes() != null) {
            	 try {
						estudiantesEmails = usuarioClient.crearCascaras(grupoDTO.getEstudiantes()).stream()
								.map(e -> e.getEmail()).collect(Collectors.toSet());
                 } catch (Exception e) {
                     throw new RuntimeException("Error al crear estudiantes desde ms-auth: " + e.getMessage(), e);
                 }
            }
            
            nuevoGrupo.setEstudiantesEmails(estudiantesEmails);
            
            // Guardar grupo
            Grupo grupoGuardado = grupoRepository.save(nuevoGrupo);
            
            return ResponseEntity.ok(grupoGuardado);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear grupo: " + e.getMessage());
        }
    }
    
    @GetMapping
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> listarGrupos() {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            ProfesorDTO profesor = usuarioService.obtenerProfesor(profesorEmail);
            
            List<Grupo> grupos = grupoRepository.findByProfesorEmail(profesorEmail);
            List<GrupoResumidoDTO> gruposDTO = new ArrayList<>();
            
            for (Grupo grupo : grupos) {
                GrupoResumidoDTO grupoDTO = new GrupoResumidoDTO();
                grupoDTO.setId(grupo.getId());
                grupoDTO.setNombre(grupo.getNombre());
                grupoDTO.setNumEstudiantes(grupo.getEstudiantesEmails().size());
                grupoDTO.setProfesorEmail(profesor.getEmail());
                grupoDTO.setProfesorNombre(profesor.getNombre());
                gruposDTO.add(grupoDTO);
            }
            
            return ResponseEntity.ok(gruposDTO);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al listar grupos: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> consultarGrupoPorId(@PathVariable int id) {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            Optional<Grupo> grupoOpt = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);
            
            if (!grupoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Grupo no encontrado con el ID: " + id);
            }
            
            return ResponseEntity.ok(grupoOpt.get());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al consultar grupo: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarGrupo(@PathVariable int id) {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            Optional<Grupo> grupoOpt = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);
            
            if (!grupoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Grupo no encontrado con el ID: " + id);
            }
            
            grupoRepository.deleteById(id);
            
            return ResponseEntity.ok().body("Grupo eliminado exitosamente.");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar grupo: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarGrupo(@PathVariable int id, @RequestBody GrupoDTO grupoDTO) {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            Optional<Grupo> grupoOptional = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);
            
            if (!grupoOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Grupo no encontrado con el ID: " + id);
            }
            
            Grupo grupoExistente = grupoOptional.get();
            grupoExistente.setNombre(grupoDTO.getNombre());
            
            // Actualizar estudiantes
            Set<String> estudiantesEmails = new HashSet<>();
            
            if (grupoDTO.getEstudiantes() != null) {
                for (EstudianteCrearDTO estud : grupoDTO.getEstudiantes()) {
                    estudiantesEmails.add(estud.getEmail());
                }
            }
            
            grupoExistente.setEstudiantesEmails(estudiantesEmails);
            
            return ResponseEntity.ok(grupoRepository.save(grupoExistente));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar grupo: " + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/estudiantes")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> agregarEstudiantesAlGrupo(@PathVariable int id,
                                                      @RequestBody List<EstudianteCrearDTO> cascaras) {
        try {
            // Obtener el email del profesor de la autenticación de forma segura
            String profesorEmail = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName(); // generalmente devuelve el username/email

            Optional<Grupo> grupoOptional = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);

            if (!grupoOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Grupo no encontrado con el ID: " + id);
            }

            Grupo grupo = grupoOptional.get();



            
                try {
                	usuarioClient.crearCascaras(cascaras);
					// Agregar solo los emails que no están ya en el grupo
					for (EstudianteCrearDTO e : cascaras) {
						grupo.getEstudiantesEmails().add(e.getEmail());
					}
                } catch (Exception e) {
                    throw new RuntimeException("Error al crear estudiantes desde ms-auth: " + e.getMessage(), e);
                }
            


            Grupo guardado = grupoRepository.save(grupo);
            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al agregar estudiantes: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}/estudiantes/{email}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarEstudianteDelGrupo(@PathVariable int id, 
                                                          @PathVariable String email) {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            Optional<Grupo> grupoOptional = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);
            
            if (!grupoOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Grupo no encontrado con el ID: " + id);
            }
            
            Grupo grupo = grupoOptional.get();
            
            if (!grupo.getEstudiantesEmails().contains(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El estudiante no pertenece a este grupo");
            }
            
            grupo.getEstudiantesEmails().remove(email);
            
            return ResponseEntity.ok(grupoRepository.save(grupo));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar estudiante del grupo: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}/estudiantes")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarEstudiantesDelGrupo(@PathVariable int id, 
                                                           @RequestBody List<String> emails) {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            Optional<Grupo> grupoOptional = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);
            
            if (!grupoOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Grupo no encontrado con el ID: " + id);
            }
            
            Grupo grupo = grupoOptional.get();
            
            List<String> emailsNoEncontrados = new ArrayList<>();
            
            for (String email : emails) {
                if (grupo.getEstudiantesEmails().contains(email)) {
                    grupo.getEstudiantesEmails().remove(email);
                } else {
                    emailsNoEncontrados.add(email);
                }
            }
            
            if (!emailsNoEncontrados.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Estudiantes no encontrados en el grupo: " + emailsNoEncontrados.toString());
            }
            
            return ResponseEntity.ok(grupoRepository.save(grupo));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar estudiantes: " + e.getMessage());
        }
    }
    
    // Endpoint adicional: Obtener estudiantes de un grupo con información completa
    @GetMapping("/{id}/estudiantes")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> obtenerEstudiantesDelGrupo(@PathVariable int id) {
        try {
            String profesorEmail = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
            
            Optional<Grupo> grupoOptional = grupoRepository.findByProfesorEmailAndId(profesorEmail, id);
            
            if (!grupoOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Grupo no encontrado con el ID: " + id);
            }
            
            Grupo grupo = grupoOptional.get();
            
            // Obtener información completa de cada estudiante desde ms-auth
            List<EstudianteDTO> estudiantes = new ArrayList<>();
            
            for (String email : grupo.getEstudiantesEmails()) {
                try {
                    EstudianteDTO estudiante = usuarioService.obtenerEstudiante(email);
                    if (estudiante != null) {
                        estudiantes.add(estudiante);
                    }
                } catch (Exception e) {
                    // Log error pero continuar con los demás estudiantes
                    System.err.println("Error al obtener estudiante " + email + ": " + e.getMessage());
                }
            }
            
            return ResponseEntity.ok(estudiantes);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener estudiantes del grupo: " + e.getMessage());
        }
    }
}