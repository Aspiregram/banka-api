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

    private final PaisService paisServ;

    public PaisController(PaisService paisServ) {
        this.paisServ = paisServ;
    }

    @PreAuthorize("hasRole('ONG')")
    @PostMapping
    public ResponseEntity<PaisDto> savePais(@RequestBody PaisDto paisDto) {
        PaisDto paisCriado = paisServ.save(paisDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(paisCriado);
    }

    @PreAuthorize("hasRole('ONG')")
    @GetMapping
    public ResponseEntity<List<PaisDto>> findAllPaises() {
        List<PaisDto> paisesEncontrados = paisServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(paisesEncontrados);
    }

    @PreAuthorize("hasRole('ONG')")
    @GetMapping("/{id}")
    public ResponseEntity<PaisDto> findPaisById(@PathVariable Long id) {
        PaisDto paisEncontrado = paisServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(paisEncontrado);
    }

    @PreAuthorize("hasRole('ONG')")
    @PutMapping("/{id}")
    public ResponseEntity<PaisDto> updatePais(
            @PathVariable Long id,
            @RequestBody PaisDto paisDto) {
        PaisDto paisAtualizado = paisServ.update(id, paisDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paisAtualizado);
    }

    @PreAuthorize("hasRole('ONG')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaisById(@PathVariable Long id) {
        paisServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
