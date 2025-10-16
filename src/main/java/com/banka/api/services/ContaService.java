package com.banka.api.services;

import com.banka.api.models.Conta;
import com.banka.api.records.ContaDto;
import com.banka.api.repositories.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository contaRepo;

    public ContaService(ContaRepository contaRepo) {
        this.contaRepo = contaRepo;
    }

    @Transactional
    public ContaDto save(ContaDto contaDto) {
        if (contaRepo.existsByUsuarioIdAndMoedaId
                (contaDto.usuario().getId(), contaDto.moeda().getId())) {
            throw new RuntimeException("O usuário já possui uma conta nesta moeda");
        }

        Conta conta = new Conta(
                null,
                contaDto.usuario(),
                contaDto.moeda(),
                null,
                null
        );

        Conta contaSalva = contaRepo.save(conta);

        return toDto(contaSalva);
    }

    public List<ContaDto> findAll() {
        return contaRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ContaDto findById(UUID id) {
        return toDto(findEntityById(id));
    }

    private Conta findEntityById(UUID id) {
        return contaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta bancária não encontrada"));
    }

    @Transactional
    public ContaDto updateSaldo(UUID id, BigDecimal valor) {
        Conta conta = findEntityById(id);

        if (conta.getSaldo().add(valor).compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Saldo insuficiente para esta operação");


        conta.setSaldo(conta.getSaldo().add(valor));
        Conta contaAtualizada = contaRepo.save(conta);

        return toDto(contaAtualizada);
    }

    public void deleteById(UUID id) {
        if (!contaRepo.existsById(id))
            throw new RuntimeException("Conta não existe");

        contaRepo.deleteById(id);
    }

    private ContaDto toDto(Conta conta) {
        return new ContaDto(
                conta.getUsuario(),
                conta.getMoeda(),
                conta.getSaldo()
        );
    }

}
