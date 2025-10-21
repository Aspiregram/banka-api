package com.banka.api.controllers;

import com.banka.api.records.log.LogSenhaCreateDto;
import com.banka.api.records.log.LogSenhaResponseDto;
import com.banka.api.services.LogSenhaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogSenhaController {
    private final LogSenhaService logSenhaServ;

    public LogSenhaController(LogSenhaService logSenhaServ) {
        this.logSenhaServ = logSenhaServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<LogSenhaResponseDto> createLog(@RequestBody LogSenhaCreateDto usuCreateDto) {
        LogSenhaResponseDto logCriado = logSenhaServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(logCriado);
    }

    @GetMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<List<LogSenhaResponseDto>> listAllLogs() {
        List<LogSenhaResponseDto> logsListados = logSenhaServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(logsListados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<LogSenhaResponseDto> findLogById(@PathVariable Long id) {
        LogSenhaResponseDto logEncontrado = logSenhaServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(logEncontrado);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<Void> deleteAllLogs() {
        logSenhaServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ONG')")
    public ResponseEntity<Void> deleteLogById(@PathVariable Long id) {
        logSenhaServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
