package com.banka.api.controllers;

import com.banka.api.records.ContaDto;
import com.banka.api.services.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaDto> createConta(@RequestBody ContaDto contaDto) {
        ContaDto novaConta = contaService.save(contaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    public ResponseEntity<List<ContaDto>> getAllContas() {
        List<ContaDto> contas = contaService.findAll();
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDto> getContaById(@PathVariable String id) {
        ContaDto conta = contaService.findById(id);
        return ResponseEntity.ok(conta);
    }

    @PutMapping("/{id}/saldo")
    public ResponseEntity<ContaDto> updateSaldo(
            @PathVariable String id,
            @RequestBody BigDecimal valor) {

        ContaDto contaAtualizada = contaService.updateSaldo(id, valor);
        return ResponseEntity.ok(contaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable String id) {
        contaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}