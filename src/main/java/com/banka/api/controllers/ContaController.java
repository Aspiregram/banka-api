package com.banka.api.controllers;

import com.banka.api.records.ContaDto;
import com.banka.api.services.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @PostMapping
    public ResponseEntity<ContaDto> createConta(@RequestBody ContaDto contaDto) {
        ContaDto novaConta = contaService.save(contaDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @GetMapping
    public ResponseEntity<List<ContaDto>> getAllContas() {
        List<ContaDto> contas = contaService.findAll();

        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDto> getContaById(@PathVariable UUID id) {
        ContaDto conta = contaService.findById(id);

        return ResponseEntity.ok(conta);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @PutMapping("/{id}/saldo")
    public ResponseEntity<ContaDto> updateSaldo(
            @PathVariable UUID id,
            @RequestBody BigDecimal valor) {

        ContaDto contaAtualizada = contaService.updateSaldo(id, valor);

        return ResponseEntity.ok(contaAtualizada);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable UUID id) {
        contaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
