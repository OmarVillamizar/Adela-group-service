package com.example.chaea.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chaea.dto.UsuarioReferenciaRequest;
import com.example.chaea.entities.UsuarioReferencia;
import com.example.chaea.repositories.UsuarioReferenciaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioReferenciaService {

    private final UsuarioReferenciaRepository repository;

    public UsuarioReferencia registrar(UsuarioReferenciaRequest request) {
        if (!repository.existsById(request.getEmail())) {
            UsuarioReferencia usuario = UsuarioReferencia.builder()
                    .email(request.getEmail())
                    .tipoUsuario(request.getTipoUsuario())
                    .build();
            return repository.save(usuario);
        }
        return repository.findById(request.getEmail()).get();
    }
}
