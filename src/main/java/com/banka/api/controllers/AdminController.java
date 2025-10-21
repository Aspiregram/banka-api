package com.banka.api.controllers;

import com.banka.api.records.admin.AdminCreateDto;
import com.banka.api.records.admin.AdminResponseDto;
import com.banka.api.records.admin.AdminUpdateDto;
import com.banka.api.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminServ;

    public AdminController(AdminService adminServ) {
        this.adminServ = adminServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody AdminCreateDto usuCreateDto) {
        AdminResponseDto adminCriado = adminServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(adminCriado);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminResponseDto>> listAllAdmins() {
        List<AdminResponseDto> adminsListados = adminServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(adminsListados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDto> findAdminById(@PathVariable Long id) {
        AdminResponseDto adminEncontrado = adminServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(adminEncontrado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminResponseDto> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminUpdateDto usuUptDto
    ) {
        AdminResponseDto adminAtualizado = adminServ.update(id, usuUptDto);

        return ResponseEntity.ok(adminAtualizado);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAllAdmins() {
        adminServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdminById(@PathVariable Long id) {
        adminServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
