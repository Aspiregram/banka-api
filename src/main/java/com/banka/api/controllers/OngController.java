package com.banka.api.controllers;

import com.banka.api.records.ong.OngCreateDto;
import com.banka.api.records.ong.OngResponseDto;
import com.banka.api.records.ong.OngUpdateDto;
import com.banka.api.services.OngService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ongs")
public class OngController {
    private final OngService ongServ;

    public OngController(OngService ongServ) {
        this.ongServ = ongServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OngResponseDto> createOng(@RequestBody OngCreateDto usuCreateDto) {
        OngResponseDto ongCriada = ongServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ongCriada);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OngResponseDto>> listAllOngs() {
        List<OngResponseDto> ongsListadas = ongServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(ongsListadas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OngResponseDto> findOngById(@PathVariable UUID id) {
        OngResponseDto ongEncontrada = ongServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(ongEncontrada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OngResponseDto> updateOng(
            @PathVariable UUID id,
            @RequestBody OngUpdateDto usuUptDto
    ) {
        OngResponseDto ongAtualizada = ongServ.update(id, usuUptDto);

        return ResponseEntity.ok(ongAtualizada);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAllOngs() {
        ongServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOngById(@PathVariable UUID id) {
        ongServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
