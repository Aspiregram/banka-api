package com.banka.api.controllers;

import com.banka.api.records.UsuarioDto;
import com.banka.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuServ;

    public UsuarioController(UsuarioService usuServ) {
        this.usuServ = usuServ;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> saveUsuario(@RequestBody UsuarioDto usuDto) {
        UsuarioDto usuCriado = usuServ.save(usuDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuCriado);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAllUsuarios() {
        List<UsuarioDto> ususEncontrados = usuServ.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(ususEncontrados);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findUsuarioById(@PathVariable UUID id, Authentication authentication) {
        UsuarioDto usuEncontrado = usuServ.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(usuEncontrado);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDto> findUsuarioByEmail(@PathVariable String email) {
        UsuarioDto usuEncontrado = usuServ.findByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(usuEncontrado);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @PathVariable UUID id,
            @RequestBody UsuarioDto usuDto) {
        UsuarioDto usuAtualizado = usuServ.update(id, usuDto);

        return ResponseEntity.status(HttpStatus.OK).body(usuAtualizado);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioById(@PathVariable UUID id) {
        usuServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
