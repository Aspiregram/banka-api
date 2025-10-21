package com.banka.api.controllers;

import com.banka.api.records.pais.PaisCreateDto;
import com.banka.api.records.pais.PaisResponseDto;
import com.banka.api.records.pais.PaisUpdateDto;
import com.banka.api.services.PaisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paises")
public class PaisController {
    private final PaisService paisServ;

    public PaisController(PaisService paisServ) {
        this.paisServ = paisServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaisResponseDto> createPais(@RequestBody PaisCreateDto usuCreateDto) {
        PaisResponseDto paisCriado = paisServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(paisCriado);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG')")
    public ResponseEntity<List<PaisResponseDto>> listAllPaises() {
        List<PaisResponseDto> paisesListados = paisServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(paisesListados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG')")
    public ResponseEntity<PaisResponseDto> findPaisById(@PathVariable Long id) {
        PaisResponseDto paisEncontrado = paisServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(paisEncontrado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaisResponseDto> updatePais(
            @PathVariable Long id,
            @RequestBody PaisUpdateDto usuUptDto
    ) {
        PaisResponseDto paisAtualizado = paisServ.update(id, usuUptDto);

        return ResponseEntity.ok(paisAtualizado);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAllPaises() {
        paisServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePaisById(@PathVariable Long id) {
        paisServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
