package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Conta;
import com.banka.api.records.conta.ContaCreateDto;
import com.banka.api.records.conta.ContaResponseDto;
import com.banka.api.records.conta.ContaUpdateDto;
import com.banka.api.repositories.ClienteRepository;
import com.banka.api.repositories.ContaRepository;
import com.banka.api.repositories.MoedaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {
    private final ContaRepository contaRepo;
    private final ClienteRepository clienteRepo;
    private final MoedaRepository moedaRepo;

    public ContaService(ContaRepository contaRepo, ClienteRepository clienteRepo,
                        MoedaRepository moedaRepo) {
        this.contaRepo = contaRepo;
        this.clienteRepo = clienteRepo;
        this.moedaRepo = moedaRepo;
    }

    // POST
    @Transactional
    public ContaResponseDto save(ContaCreateDto contaCrtDto) {
        var cliente = clienteRepo.findById(contaCrtDto.cliente())
                .orElseThrow(() -> new EntityNotFoundException(
                        "O cliente com ID \"" + contaCrtDto.cliente() + "\" não pode ser encontrado"));

        if (contaRepo.existsByCliente(cliente))
            throw new ResourceConflictException("Uma conta já possui esse cliente");

        Conta conta = fromCreateDto(contaCrtDto);
        Conta contaSalva = contaRepo.save(conta);

        return toResponseDto(contaSalva);
    }

    // GET
    public List<ContaResponseDto> findAll() {
        List<Conta> contas = contaRepo.findAll();

        if (contas.isEmpty())
            throw new EntityNotFoundException
                    ("Não há contas registradas");

        return contas.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public ContaResponseDto findById(Long id) {
        Conta contaEncontrada = contaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A conta com ID \"" + id + "\" não pode ser encontrada"));

        return toResponseDto(contaEncontrada);
    }

    // PUT
    @Transactional
    public ContaResponseDto update(Long id, ContaUpdateDto contaUptDto) {
        Conta contaEncontrada = contaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A conta com ID \"" + id + "\" não pode ser encontrada"));

        contaEncontrada.setCliente(clienteRepo.findById(contaUptDto.cliente())
                .orElseThrow(() -> new EntityNotFoundException
                        ("O cliente com ID \"" + contaUptDto.cliente() + "\" não pode ser encontrado")));
        contaEncontrada.setMoeda(moedaRepo.findById(contaUptDto.cliente())
                .orElseThrow(() -> new EntityNotFoundException
                        ("A moeda com ID \"" + contaUptDto.moeda() + "\" não pode ser encontrada")));
        contaEncontrada.setSaldo(contaUptDto.saldo());

        Conta contaAtualizada = contaRepo.save(contaEncontrada);

        return toResponseDto(contaAtualizada);
    }

    // DELETE
    public void deleteAll() {
        List<Conta> contas = contaRepo.findAll();

        if (contas.isEmpty())
            throw new EntityNotFoundException
                    ("Não há contas registradas");

        contaRepo.deleteAll();
    }

    // DELETE
    public void deleteById(Long id) {
        if (contaRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("A conta com ID \"" + id + "\" não pode ser encontrada");

        contaRepo.deleteById(id);
    }

    private Conta fromCreateDto(ContaCreateDto contaCrtDto) {
        return new Conta(
                null,
                clienteRepo.findById(contaCrtDto.cliente())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O cliente com ID \"" + contaCrtDto.cliente() + "\" não pode ser encontrado")),
                moedaRepo.findById(contaCrtDto.moeda())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A moeda com ID \"" + contaCrtDto.moeda() + "\" não pode ser encontrada")),
                contaCrtDto.saldo(),
                null);
    }

    private ContaResponseDto toResponseDto(Conta conta) {
        return new ContaResponseDto(
                conta.getId(),
                conta.getCliente().getId(),
                conta.getMoeda().getId(),
                conta.getSaldo(),
                conta.getCriadaEm()
        );
    }
}
