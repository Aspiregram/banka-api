package com.banka.api.controllers;

import com.banka.api.records.transacao.TransacaoCreateDto;
import com.banka.api.records.transacao.TransacaoResponseByOngDto;
import com.banka.api.records.transacao.TransacaoResponseDto;
import com.banka.api.services.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoServ;

    public TransacaoController(TransacaoService transacaoServ) {
        this.transacaoServ = transacaoServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<TransacaoResponseDto> createTransacao(@RequestBody TransacaoCreateDto usuCreateDto) {
        TransacaoResponseDto transacaoCriada = transacaoServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoCriada);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ONG', 'CLIENTE')")
    public ResponseEntity<List<TransacaoResponseDto>> listAllTransacoes() {
        List<TransacaoResponseDto> transacoesListadas = transacaoServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(transacoesListadas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ONG', 'CLIENTE')")
    public ResponseEntity<TransacaoResponseDto> findTransacaoById(@PathVariable Long id) {
        TransacaoResponseDto transacaoEncontrada = transacaoServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(transacaoEncontrada);
    }

    @GetMapping("/por-ong/{ongId}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<List<TransacaoResponseByOngDto>> findTransacaoByOngId(@PathVariable Long ongId) {
        List<TransacaoResponseByOngDto> transacoesListadas = transacaoServ.findByOngId(ongId);

        return ResponseEntity.status(HttpStatus.FOUND).body(transacoesListadas);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<Void> deleteAllTransacoes() {
        transacaoServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<Void> deleteTransacaoById(@PathVariable Long id) {
        transacaoServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
