package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Cliente;
import com.banka.api.records.cliente.ClienteCreateDto;
import com.banka.api.records.cliente.ClienteResponseDto;
import com.banka.api.records.cliente.ClienteUpdateDto;
import com.banka.api.records.usuario.UsuarioResponseDto;
import com.banka.api.repositories.ClienteRepository;
import com.banka.api.repositories.OngRepository;
import com.banka.api.repositories.PaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepo;
    private final PasswordEncoder passEncod;
    private final PaisRepository paisRepo;
    private final OngRepository ongRepo;

    public ClienteService(ClienteRepository clienteRepo, PasswordEncoder passEncod,
                          PaisRepository paisRepo, OngRepository ongRepo) {
        this.clienteRepo = clienteRepo;
        this.passEncod = passEncod;
        this.paisRepo = paisRepo;
        this.ongRepo = ongRepo;
    }

    // POST
    @Transactional
    public ClienteResponseDto save(ClienteCreateDto clienteCreateDto) {
        if (clienteRepo.existsByUsername(clienteCreateDto.usuCreateDto().username())
                || clienteRepo.existsByEmail(clienteCreateDto.usuCreateDto().email()))
            throw new ResourceConflictException
                    ("Um cliente já possui esse username e/ou email");

        Cliente cliente = fromCreateDto(clienteCreateDto);

        cliente.setId(null);
        cliente.setUsername(clienteCreateDto.usuCreateDto().username());
        cliente.setNome(clienteCreateDto.usuCreateDto().nome());
        cliente.setSobrenome(clienteCreateDto.usuCreateDto().sobrenome());
        cliente.setEmail(clienteCreateDto.usuCreateDto().email());
        cliente.setSenha(passEncod.encode(
                clienteCreateDto.usuCreateDto().senha()));
        cliente.setRole(null);
        cliente.setCriadoEm(null);
        cliente.setUltimoLogin(null);

        Cliente clienteSalvo = clienteRepo.save(cliente);

        return toResponseDto(clienteSalvo);
    }

    // GET
    public List<ClienteResponseDto> findAll() {
        List<Cliente> clientes = clienteRepo.findAll();

        if (clientes.isEmpty())
            throw new EntityNotFoundException
                    ("Não há clientes registrados");

        return clientes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public ClienteResponseDto findById(Long id) {
        Cliente clienteEncontrado = clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O cliente com ID \"" + id + "\" não pode ser encontrado"));

        return toResponseDto(clienteEncontrado);
    }

    // PUT
    @Transactional
    public ClienteResponseDto update(Long id, ClienteUpdateDto clienteUptDto) {
        Cliente clienteEncontrado = clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O cliente com ID \"" + id + "\" não pode ser encontrado"));

        clienteEncontrado.setUsername(clienteUptDto.usuUpdateDto().username());
        clienteEncontrado.setNome(clienteUptDto.usuUpdateDto().nome());
        clienteEncontrado.setSobrenome(clienteUptDto.usuUpdateDto().sobrenome());
        clienteEncontrado.setEmail(clienteUptDto.usuUpdateDto().email());
        clienteEncontrado.setSenha(
                passEncod.encode(clienteUptDto.usuUpdateDto().senha()));
        clienteEncontrado.setDocumento(clienteUptDto.documento());
        clienteEncontrado.setPaisOrigem(
                paisRepo.findById(clienteUptDto.paisOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país originário com ID \"" + clienteUptDto.paisOrigem()
                                        + "\" não pode ser encontrado")));
        clienteEncontrado.setPaisAtual(
                paisRepo.findById(clienteUptDto.paisOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país atual com ID \"" + clienteUptDto.paisAtual()
                                        + "\" não pode ser encontrado")));
        clienteEncontrado.setOng(
                ongRepo.findById(clienteUptDto.paisOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A ONG com ID \"" + clienteUptDto.ong()
                                        + "\" não pode ser encontrada")));

        Cliente clienteAtualizado = clienteRepo.save(clienteEncontrado);

        return toResponseDto(clienteAtualizado);
    }

    // DELETE
    public void deleteAll() {
        List<Cliente> clientes = clienteRepo.findAll();

        if (clientes.isEmpty())
            throw new EntityNotFoundException
                    ("Não há clientes registrados");

        clienteRepo.deleteAll();
    }

    // DELETE
    public void deleteById(Long id) {
        if (clienteRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("O cliente com ID \"" + id + "\" não pode ser encontrado");

        clienteRepo.deleteById(id);
    }

    private Cliente fromCreateDto(ClienteCreateDto clienteCreateDto) {
        return new Cliente(
                clienteCreateDto.documento(),
                paisRepo.findById(clienteCreateDto.paisOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país com ID \"" + clienteCreateDto.paisOrigem()
                                        + "\" não pode ser encontrado")),
                paisRepo.findById(clienteCreateDto.paisAtual())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país com ID \"" + clienteCreateDto.paisAtual()
                                        + "\" não pode ser encontrado")),
                ongRepo.findById(clienteCreateDto.ong())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A ONG com ID \"" + clienteCreateDto.ong()
                                        + "\" não pode ser encontrada")),
                null
        );
    }

    private ClienteResponseDto toResponseDto(Cliente cliente) {
        return new ClienteResponseDto(
                new UsuarioResponseDto(
                        cliente.getId(),
                        cliente.getUsername(),
                        cliente.getNome(),
                        cliente.getSobrenome(),
                        cliente.getEmail(),
                        cliente.getRole(),
                        cliente.getCriadoEm(),
                        cliente.getUltimoLogin()
                ),
                cliente.getDocumento(),
                cliente.getPaisOrigem().getId(),
                cliente.getPaisAtual().getId(),
                cliente.getOng().getId(),
                cliente.getEstaAtivo()
        );
    }
}
