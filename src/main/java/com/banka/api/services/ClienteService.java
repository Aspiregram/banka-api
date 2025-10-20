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
import com.banka.api.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepo;
    private final UsuarioRepository usuRepo;
    private final PasswordEncoder passEncod;
    private final PaisRepository paisRepo;
    private final OngRepository ongRepo;

    public ClienteService(ClienteRepository clienteRepo, UsuarioRepository usuRepo,
                          PasswordEncoder passEncod, PaisRepository paisRepo,
                          OngRepository ongRepo) {
        this.clienteRepo = clienteRepo;
        this.usuRepo = usuRepo;
        this.passEncod = passEncod;
        this.paisRepo = paisRepo;
        this.ongRepo = ongRepo;
    }

    // POST
    @Transactional
    public ClienteResponseDto save(ClienteCreateDto clienteCreateDto) {
        if (usuRepo.existsByEmail(clienteCreateDto.usuCreateDto().email()))
            throw new ResourceConflictException
                    ("Um cliente já possui esse email");

        Cliente cliente = fromCreateDto(clienteCreateDto);
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
    public ClienteResponseDto findById(UUID id) {
        Cliente clienteEncontrado = clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O cliente com ID \"" + id + "\" não pode ser encontrado"));

        return toResponseDto(clienteEncontrado);
    }

    // PUT
    @Transactional
    public ClienteResponseDto update(UUID id, ClienteUpdateDto clienteUptDto) {
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
    public void deleteById(UUID id) {
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
                                        + "\" não pode ser encontrad")),
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
                        cliente.getFaceHash(),
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
