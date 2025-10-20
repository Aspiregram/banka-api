package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Conta;
import com.banka.api.models.Moeda;
import com.banka.api.models.Transacao;
import com.banka.api.records.conta.ContaCreateDto;
import com.banka.api.records.conta.ContaResponseDto;
import com.banka.api.records.conta.ContaUpdateDto;
import com.banka.api.repositories.ClienteRepository;
import com.banka.api.repositories.ContaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContaService {
    private final ContaRepository contaRepo;
    private final ClienteRepository clienteRepo;

    public ContaService(ContaRepository contaRepo, ClienteRepository clienteRepo) {
        this.contaRepo = contaRepo;
        this.clienteRepo = clienteRepo;
    }

    // POST
    @Transactional
    public ContaResponseDto save(ContaCreateDto contaCreateDto) {
        if (contaRepo.existsByCliente(contaCreateDto.cliente()))
            throw new ResourceConflictException
                    ("Uma conta já possui esse cliente");

        Conta conta = fromCreateDto(contaCreateDto);
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
    public ContaResponseDto findById(UUID id) {
        Conta contaEncontrada = contaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A conta com ID \"" + id + "\" não pode ser encontrada"));

        return toResponseDto(contaEncontrada);
    }

    // PUT
    @Transactional
    public ContaResponseDto update(UUID id, ContaUpdateDto contaUptDto) {
        Conta contaEncontrada = contaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A conta com ID \"" + id + "\" não pode ser encontrada"));


        contaEncontrada.setCliente(clienteRepo.findById(contaUptDto.cliente())
                .orElseThrow(() -> new EntityNotFoundException
                        ("O cliente com ID \"" + contaUptDto.cliente() + "\" não pode ser encontrado")));
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
    public void deleteById(UUID id) {
        if (contaRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("A conta com ID \"" + id + "\" não pode ser encontrada");

        contaRepo.deleteById(id);
    }

    private Conta fromCreateDto(ContaCreateDto contaCreateDto) {
        return new Conta(
                null,
                clienteRepo.findById(contaCreateDto.cliente())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O cliente com ID \"" + contaCreateDto.cliente() + "\" não pode ser encontrado")),
                null,
                null,
                contaCreateDto.saldo(),
                null);
    }

    private ContaResponseDto toResponseDto(Conta conta) {
        return new ContaResponseDto(
                conta.getId(),
                conta.getCliente().getId(),
                conta.getMoedas()
                        .stream()
                        .map(Moeda::getId)
                        .collect(Collectors.toSet()),
                conta.getTransacoes()
                        .stream()
                        .map(Transacao::getId)
                        .collect(Collectors.toSet()),
                conta.getSaldo(),
                conta.getCriadoEm()
        );
    }
}
