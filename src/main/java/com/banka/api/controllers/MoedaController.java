package com.banka.api.controllers;

import com.banka.api.records.moeda.MoedaCreateDto;
import com.banka.api.records.moeda.MoedaResponseDto;
import com.banka.api.records.moeda.MoedaUpdateDto;
import com.banka.api.services.MoedaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/moedas")
public class MoedaController {
    private final MoedaService moedaServ;

    public MoedaController(MoedaService moedaServ) {
        this.moedaServ = moedaServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MoedaResponseDto> createMoeda(@RequestBody MoedaCreateDto usuCreateDto) {
        MoedaResponseDto moedaCriada = moedaServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(moedaCriada);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MoedaResponseDto>> listAllMoedas() {
        List<MoedaResponseDto> moedasListadas = moedaServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(moedasListadas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MoedaResponseDto> findMoedaById(@PathVariable UUID id) {
        MoedaResponseDto moedaEncontrada = moedaServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(moedaEncontrada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MoedaResponseDto> updateMoeda(
            @PathVariable UUID id,
            @RequestBody MoedaUpdateDto usuUptDto
    ) {
        MoedaResponseDto moedaAtualizada = moedaServ.update(id, usuUptDto);

        return ResponseEntity.ok(moedaAtualizada);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAllMoedas() {
        moedaServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMoedaById(@PathVariable UUID id) {
        moedaServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
