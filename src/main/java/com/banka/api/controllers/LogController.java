package com.banka.api.controllers;

import com.banka.api.records.LogDto;
import com.banka.api.services.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logServ;

    public LogController(LogService logServ) {
        this.logServ = logServ;
    }

    @PreAuthorize("hasRole('ONG')")
    @PostMapping
    public ResponseEntity<LogDto> saveLog(@RequestBody LogDto logDto) {
        LogDto logCriado = logServ.save(logDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(logCriado);
    }

    @PreAuthorize("hasRole('ONG')")
    @GetMapping
    public ResponseEntity<List<LogDto>> findAllLogs() {
        List<LogDto> logsEncontrados = logServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(logsEncontrados);
    }

    @PreAuthorize("hasRole('ONG')")
    @GetMapping("/{id}")
    public ResponseEntity<LogDto> findLogById(@PathVariable Long id) {
        LogDto logEncontrado = logServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(logEncontrado);
    }

    @PreAuthorize("hasRole('ONG')")
    @PutMapping("/{id}")
    public ResponseEntity<LogDto> updateLog(
            @PathVariable Long id,
            @RequestBody LogDto logDto) {
        LogDto logAtualizado = logServ.update(id, logDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(logAtualizado);
    }

    @PreAuthorize("hasRole('ONG')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogById(@PathVariable Long id) {
        logServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
