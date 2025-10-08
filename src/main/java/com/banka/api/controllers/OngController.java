package com.banka.api.controllers;

import com.banka.api.records.OngDto;
import com.banka.api.services.OngService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ongs")
public class OngController {

    private final OngService ongServ;

    public OngController(OngService ongServ) {
        this.ongServ = ongServ;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<OngDto> saveOng(@RequestBody OngDto ongDto) {
        OngDto ongCriada = ongServ.save(ongDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ongCriada);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OngDto>> findAllOngs() {
        List<OngDto> ongsEncontradas = ongServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(ongsEncontradas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OngDto> findOngById(@PathVariable Long id) {
        OngDto ongEncontrada = ongServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(ongEncontrada);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{email}")
    public ResponseEntity<OngDto> findOngByEmail(@PathVariable String email) {
        OngDto ongEncontrada = ongServ.findByEmail(email);

        return ResponseEntity.status(HttpStatus.FOUND).body(ongEncontrada);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OngDto> updateOng(
            @PathVariable Long id,
            @RequestBody OngDto ongDto) {
        OngDto ongAtualizada = ongServ.update(id, ongDto);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ongAtualizada);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOngById(@PathVariable Long id) {
        ongServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
