package com.banka.api.controllers;

import com.banka.api.records.LogDto;
import com.banka.api.services.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logServ;

    public LogController(LogService logServ) {
        this.logServ = logServ;
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @PostMapping
    public ResponseEntity<LogDto> saveLog(@RequestBody LogDto logDto) {
        LogDto logCriado = logServ.save(logDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(logCriado);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @GetMapping
    public ResponseEntity<List<LogDto>> findAllLogs() {
        List<LogDto> logsEncontrados = logServ.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(logsEncontrados);
    }

    @PreAuthorize("hasRole('ROLE_ONG')")
    @GetMapping("/{id}")
    public ResponseEntity<LogDto> findLogById(@PathVariable UUID id) {
        LogDto logEncontrado = logServ.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(logEncontrado);
    }

}
