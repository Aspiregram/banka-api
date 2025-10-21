package com.banka.api.controllers;

import com.banka.api.records.conta.ContaCreateDto;
import com.banka.api.records.conta.ContaResponseDto;
import com.banka.api.records.conta.ContaUpdateDto;
import com.banka.api.services.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {
    private final ContaService contaServ;

    public ContaController(ContaService contaServ) {
        this.contaServ = contaServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<ContaResponseDto> createConta(@RequestBody ContaCreateDto usuCreateDto) {
        ContaResponseDto contaCriada = contaServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(contaCriada);
    }

    @GetMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<List<ContaResponseDto>> listAllContas() {
        List<ContaResponseDto> contasListadas = contaServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(contasListadas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<ContaResponseDto> findContaById(@PathVariable Long id) {
        ContaResponseDto contaEncontrada = contaServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(contaEncontrada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<ContaResponseDto> updateConta(
            @PathVariable Long id,
            @RequestBody ContaUpdateDto usuUptDto
    ) {
        ContaResponseDto contaAtualizada = contaServ.update(id, usuUptDto);

        return ResponseEntity.ok(contaAtualizada);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<Void> deleteAllContas() {
        contaServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<Void> deleteContaById(@PathVariable Long id) {
        contaServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
