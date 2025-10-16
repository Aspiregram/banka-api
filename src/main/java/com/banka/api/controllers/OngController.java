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

    @PostMapping
    public ResponseEntity<OngDto> registerOng(@RequestBody OngDto ongDto) {
        OngDto ongCriada = ongServ.save(ongDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ongCriada);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<OngDto>> findAllOngs() {
        List<OngDto> ongsEncontradas = ongServ.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(ongsEncontradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OngDto> findOngById(@PathVariable String id) {
        OngDto ongEncontrada = ongServ.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ongEncontrada);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<OngDto> findOngByEmail(@PathVariable String email) {
        OngDto ongEncontrada = ongServ.findByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(ongEncontrada);
    }

    @PreAuthorize("hasRole('ONG_ADMIN') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OngDto> updateOng(
            @PathVariable String id,
            @RequestBody OngDto ongDto) {
        OngDto ongAtualizada = ongServ.update(id, ongDto);

        return ResponseEntity.status(HttpStatus.OK).body(ongAtualizada);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOngById(@PathVariable String id) {
        ongServ.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}