package com.banka.api.controllers;

import com.banka.api.records.MoedaDto;
import com.banka.api.services.MoedaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moedas")
public class MoedaController {

    private final MoedaService moedaServ;

    public MoedaController(MoedaService moedaServ) {
        this.moedaServ = moedaServ;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<MoedaDto> saveMoeda(@RequestBody MoedaDto moedaDto) {
        MoedaDto moedaCriada = moedaServ.save(moedaDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(moedaCriada);
    }

    @GetMapping
    public ResponseEntity<List<MoedaDto>> findAllMoedas() {
        List<MoedaDto> moedasEncontradas = moedaServ.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(moedasEncontradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoedaDto> findMoedaById(@PathVariable String id) {
        MoedaDto moedaEncontrada = moedaServ.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(moedaEncontrada);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MoedaDto> updateMoeda(
            @PathVariable String id,
            @RequestBody MoedaDto moedaDto) {
        MoedaDto moedaAtualizada = moedaServ.update(id, moedaDto);

        return ResponseEntity.status(HttpStatus.OK).body(moedaAtualizada);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoedaById(@PathVariable String id) {
        moedaServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}