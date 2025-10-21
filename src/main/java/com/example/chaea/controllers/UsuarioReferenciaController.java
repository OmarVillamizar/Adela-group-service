package com.example.chaea.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.chaea.dto.UsuarioReferenciaRequest;
import com.example.chaea.entities.UsuarioReferencia;
import com.example.chaea.services.UsuarioReferenciaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuario-referencia")
@RequiredArgsConstructor
public class UsuarioReferenciaController {

    private final UsuarioReferenciaService service;

    @PostMapping
    public ResponseEntity<UsuarioReferencia> registrar(@RequestBody UsuarioReferenciaRequest request) {
        return ResponseEntity.ok(service.registrar(request));
    }
}
