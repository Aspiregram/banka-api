package com.banka.api.controllers;

import com.banka.api.records.UsuarioDto;
import com.banka.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuServ;

    public UsuarioController(UsuarioService usuServ) {
        this.usuServ = usuServ;
    }

    @PreAuthorize("hasRole('ONG')")
    @PostMapping
    public ResponseEntity<UsuarioDto> saveUsuario(@RequestBody UsuarioDto usuDto) {
        UsuarioDto usuCriado = usuServ.save(usuDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuCriado);
    }

    @PreAuthorize("hasRole('ONG')")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAllUsuarios() {
        List<UsuarioDto> ususEncontrados = usuServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(ususEncontrados);
    }

    @PreAuthorize("hasRole('ONG')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findUsuarioById(@PathVariable Long id) {
        UsuarioDto usuEncontrado = usuServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(usuEncontrado);
    }

    @PreAuthorize("hasAnyRole('ONG','USER')")
    @GetMapping("/{email}")
    public ResponseEntity<UsuarioDto> findUsuarioByEmail(
            @PathVariable String email) {
        UsuarioDto usuEncontrado = usuServ.findByEmail(email);

        return ResponseEntity.status(HttpStatus.FOUND).body(usuEncontrado);
    }

    @PreAuthorize("hasRole('ONG')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDto usuDto) {
        UsuarioDto usuAtualizado = usuServ.update(id, usuDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuAtualizado);
    }

    @PreAuthorize("hasRole('ONG')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioById(@PathVariable Long id) {
        usuServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
