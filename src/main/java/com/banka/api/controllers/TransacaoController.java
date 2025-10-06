package com.banka.api.controllers;

import com.banka.api.records.TransacaoDto;
import com.banka.api.services.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transServ;

    public TransacaoController(TransacaoService transServ) {
        this.transServ = transServ;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<TransacaoDto> saveTransacao(@RequestBody TransacaoDto logDto) {
        TransacaoDto transCriada = transServ.save(logDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transCriada);
    }

    @PreAuthorize("hasAnyRole('ONG','USER')")
    @GetMapping
    public ResponseEntity<List<TransacaoDto>> findAllTransacoes() {
        List<TransacaoDto> transasEncontradas = transServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(transasEncontradas);
    }

    @PreAuthorize("hasAnyRole('ONG','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDto> findTransacaoById(@PathVariable Long id) {
        TransacaoDto transEncontrada = transServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(transEncontrada);
    }

}
