package com.banka.api.controllers;

import com.banka.api.records.usuario.UsuarioCreateDto;
import com.banka.api.records.usuario.UsuarioResponseDto;
import com.banka.api.records.usuario.UsuarioUpdateDto;
import com.banka.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioServ;

    public UsuarioController(UsuarioService usuarioServ) {
        this.usuarioServ = usuarioServ;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ONG')")
    public ResponseEntity<UsuarioResponseDto> createUsuario(@RequestBody UsuarioCreateDto usuCreateDto) {
        UsuarioResponseDto usuarioCriado = usuarioServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ONG')")
    public ResponseEntity<List<UsuarioResponseDto>> listAllUsuarios() {
        List<UsuarioResponseDto> usuariosListados = usuarioServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(usuariosListados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ONG')")
    public ResponseEntity<UsuarioResponseDto> findUsuarioById(@PathVariable UUID id) {
        UsuarioResponseDto usuarioEncontrado = usuarioServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(usuarioEncontrado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ONG')")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(
            @PathVariable UUID id,
            @RequestBody UsuarioUpdateDto usuUptDto
    ) {
        UsuarioResponseDto usuarioAtualizado = usuarioServ.update(id, usuUptDto);

        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAllUsuarios() {
        usuarioServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ONG')")
    public ResponseEntity<Void> deleteUsuarioById(@PathVariable UUID id) {
        usuarioServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
