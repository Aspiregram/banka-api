package com.banka.api.controllers;

import com.banka.api.records.cliente.ClienteCreateDto;
import com.banka.api.records.cliente.ClienteResponseDto;
import com.banka.api.records.cliente.ClienteUpdateDto;
import com.banka.api.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteServ;

    public ClienteController(ClienteService clienteServ) {
        this.clienteServ = clienteServ;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ONG')")
    public ResponseEntity<ClienteResponseDto> createCliente(@RequestBody ClienteCreateDto usuCreateDto) {
        ClienteResponseDto clienteCriado = clienteServ.save(usuCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCriado);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ONG')")
    public ResponseEntity<List<ClienteResponseDto>> listAllClientes() {
        List<ClienteResponseDto> clientesListados = clienteServ.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(clientesListados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ONG')")
    public ResponseEntity<ClienteResponseDto> findClienteById(@PathVariable UUID id) {
        ClienteResponseDto clienteEncontrado = clienteServ.findById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(clienteEncontrado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ONG')")
    public ResponseEntity<ClienteResponseDto> updateCliente(
            @PathVariable UUID id,
            @RequestBody ClienteUpdateDto usuUptDto
    ) {
        ClienteResponseDto clienteAtualizado = clienteServ.update(id, usuUptDto);

        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ONG')")
    public ResponseEntity<Void> deleteAllClientes() {
        clienteServ.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ONG')")
    public ResponseEntity<Void> deleteClienteById(@PathVariable UUID id) {
        clienteServ.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
