package com.banka.api.controllers;

import com.banka.api.records.PaisDto;
import com.banka.api.services.PaisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paises")
public class PaisController {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        this.paisService = paisService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PaisDto> createPais(@RequestBody PaisDto paisDto) {
        PaisDto novoPais = paisService.save(paisDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPais);
    }

    @GetMapping
    public ResponseEntity<List<PaisDto>> getAllPaises() {
        List<PaisDto> paises = paisService.findAll();
        return ResponseEntity.ok(paises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisDto> getPaisById(@PathVariable String id) {
        PaisDto pais = paisService.findById(id);
        return ResponseEntity.ok(pais);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PaisDto> updatePais(@PathVariable String id, @RequestBody PaisDto paisDto) {
        PaisDto paisAtualizado = paisService.update(id, paisDto);
        return ResponseEntity.ok(paisAtualizado);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePais(@PathVariable String id) {
        paisService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}