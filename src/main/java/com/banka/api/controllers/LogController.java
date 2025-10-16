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

    @PreAuthorize("hasRole('ONG_ADMIN')")
    @PostMapping
    public ResponseEntity<LogDto> saveLog(@RequestBody LogDto logDto) {
        LogDto logCriado = logServ.save(logDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(logCriado);
    }

    @PreAuthorize("hasRole('ONG_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<LogDto>> findAllLogs() {
        List<LogDto> logsEncontrados = logServ.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(logsEncontrados);
    }

    @PreAuthorize("hasRole('ONG_ADMIN') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LogDto> findLogById(@PathVariable String id) {
        LogDto logEncontrado = logServ.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(logEncontrado);
    }

}