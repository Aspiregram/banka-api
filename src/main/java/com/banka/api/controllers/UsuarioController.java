package com.banka.api.controllers;

import com.banka.api.records.UsuarioDto;
import com.banka.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('ONG_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAllUsuarios() {
        List<UsuarioDto> ususEncontrados = usuServ.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(ususEncontrados);
    }

    @PreAuthorize("hasRole('ONG_ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findUsuarioById(@PathVariable String id, Authentication authentication) {
        UsuarioDto usuEncontrado = usuServ.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(usuEncontrado);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDto> findUsuarioByEmail(@PathVariable String email) {
        UsuarioDto usuEncontrado = usuServ.findByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(usuEncontrado);
    }

    @PreAuthorize("hasRole('ONG_ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @PathVariable String id,
            @RequestBody UsuarioDto usuDto) {
        UsuarioDto usuAtualizado = usuServ.update(id, usuDto);

        return ResponseEntity.status(HttpStatus.OK).body(usuAtualizado);
    }

    @PreAuthorize("hasRole('ONG_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioById(@PathVariable String id) {
        usuServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}